package fr.vergne.dmmorpg.network.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import fr.vergne.dmmorpg.network.MessageDescriptor;
import fr.vergne.dmmorpg.network.NetworkManager;
import fr.vergne.dmmorpg.network.Peer;
import fr.vergne.logging.LoggerConfiguration;

public class SimpleNetworkManager implements NetworkManager {

	private final Peer currentPeer;
	private final ServerSocket listeningSocket;
	private final ExecutorService threadPool;
	private final Collection<MessageListener> listeners = new HashSet<MessageListener>();
	public static Logger logger = LoggerConfiguration.getSimpleLogger();

	public SimpleNetworkManager(final int portNumber) {
		try {
			this.currentPeer = new SimplePeer(InetAddress.getLocalHost()
					.getHostAddress(), portNumber);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}

		threadPool = Executors.newCachedThreadPool();
		try {
			listeningSocket = new ServerSocket();
			listeningSocket.setReuseAddress(true);
			listeningSocket.bind(new InetSocketAddress(portNumber));
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Impossible to listen on the port " + portNumber, e);
		}
		Thread listeningThread = new Thread(new Runnable() {

			@Override
			public void run() {
				logger.info(currentPeer + " starts listening...");
				while (!listeningSocket.isClosed()) {
					// TODO store and reuse connections
					final Socket clientSocket;
					try {
						clientSocket = listeningSocket.accept();
					} catch (SocketException e) {
						if (listeningSocket.isClosed()) {
							logger.warning("Interrupt listening.");
							break;
						} else {
							throw new RuntimeException(e);
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}

					threadPool.submit(new Runnable() {

						@Override
						public void run() {
							InputStream stream;
							try {
								stream = clientSocket.getInputStream();
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
							InputStreamReader streamReader = new InputStreamReader(
									stream);
							BufferedReader in = new BufferedReader(streamReader);

							StringBuffer buffer = new StringBuffer();
							String inputLine;
							String message;
							int remoteListeningPort = -1;
							try {
								while ((inputLine = in.readLine()) != null) {
									if (remoteListeningPort == -1) {
										remoteListeningPort = Integer
												.parseInt(inputLine);
									} else {
										buffer.append(inputLine);
									}
								}
								message = buffer.toString();
							} catch (IOException e) {
								throw new RuntimeException(e);
							} finally {
								try {
									in.close();
								} catch (IOException e) {
									throw new RuntimeException(e);
								}
								try {
									streamReader.close();
								} catch (IOException e) {
									throw new RuntimeException(e);
								}
								try {
									stream.close();
								} catch (IOException e) {
									throw new RuntimeException(e);
								}
							}

							String host = clientSocket.getInetAddress()
									.getHostAddress();
							Peer peer = new SimplePeer(host,
									remoteListeningPort);
							try {
								clientSocket.close();
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
							logger.fine(currentPeer + " received from " + peer
									+ ": " + message);
							for (MessageListener listener : listeners) {
								listener.messageReceived(message, peer);
							}
						}
					});
				}
				logger.info(currentPeer + " stops listening.");
			}
		});
		listeningThread.setDaemon(true);
		listeningThread.start();
	}

	public void stop() {
		threadPool.shutdown();
		try {
			listeningSocket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			stop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			super.finalize();
		}
	}

	@Override
	public Peer getCurrentPeer() {
		return currentPeer;
	}

	public int getListeningPort() {
		return currentPeer.getPort();
	}

	@Override
	public MessageDescriptor send(final String message, Peer... peers) {
		logger.fine(currentPeer + " tries to send \"" + message + "\" to "
				+ Arrays.deepToString(peers));

		final SimpleMessageDescriptor descriptor = new SimpleMessageDescriptor(
				message, Arrays.asList(peers));
		for (final Peer peer : peers) {
			threadPool.submit(new Runnable() {

				@Override
				public void run() {
					// TODO store and reuse connections
					logger.fine(currentPeer + " open a connection to " + peer);
					Socket socket;
					try {
						socket = new Socket(peer.getHost(), peer.getPort());
					} catch (UnknownHostException e) {
						throw new RuntimeException(e);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					OutputStream stream;
					try {
						stream = socket.getOutputStream();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					PrintWriter out = new PrintWriter(stream, true);

					logger.fine(currentPeer + " send to " + peer + ": "
							+ message);
					out.println("" + currentPeer.getPort());
					out.println(message);
					logger.fine(currentPeer + " has sent to " + peer + ": "
							+ message);
					descriptor.confirmSending(peer);

					// Message automatically received because socket-based
					logger.fine(currentPeer + " has confirmed reception from "
							+ peer + ": " + message);
					descriptor.confirmReception(peer);

					out.close();
					try {
						stream.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					logger.fine(currentPeer + " close connection to " + peer);
					try {
						socket.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
		logger.fine(currentPeer + " has submitted the sending threads.");
		return descriptor;
	}

	@Override
	public void register(MessageListener listener) {
		listeners.add(listener);
	}

	@Override
	public void unregister(MessageListener listener) {
		listeners.remove(listener);
	}

	private class SimpleMessageDescriptor implements MessageDescriptor {

		private final String message;
		private final Collection<Peer> targets;
		private final Collection<Peer> sentTargets;
		private final Collection<Peer> receivedTargets;
		private final Collection<MessageDescriptorListener> listeners;

		public SimpleMessageDescriptor(String message, Collection<Peer> targets) {
			this.message = message;

			this.targets = Collections.unmodifiableCollection(targets);
			this.sentTargets = new HashSet<Peer>();
			this.receivedTargets = new HashSet<Peer>();

			/*
			 * We use a linked hash set because we want to preserve the order,
			 * thus ensuring the internal listener is called first. This way,
			 * the status of the descriptor is consistent when other listeners
			 * are called.
			 */
			this.listeners = new LinkedHashSet<MessageDescriptorListener>();
			this.listeners.add(new MessageDescriptorListener() {

				@Override
				public void messageSent(Peer peer) {
					sentTargets.add(peer);
				}

				@Override
				public void messageReceived(Peer peer) {
					receivedTargets.add(peer);
				}
			});
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public Collection<Peer> getTargets() {
			return targets;
		}

		@Override
		public Collection<Peer> getSentTargets() {
			return sentTargets;
		}

		@Override
		public Collection<Peer> getReceivedTargets() {
			return receivedTargets;
		}

		@Override
		public void register(MessageDescriptorListener listener) {
			listeners.add(listener);
		}

		@Override
		public void unregister(MessageDescriptorListener listener) {
			listeners.remove(listener);
		}

		/**
		 * This method should be called when the message has been sent to a
		 * {@link Peer}. All the registered {@link MessageDescriptorListener}s
		 * are then notified through
		 * {@link MessageDescriptorListener#messageSent(Peer)}.
		 * 
		 * @param peer
		 *            the {@link Peer} confirming the reception of the message
		 */
		public void confirmSending(Peer peer) {
			for (MessageDescriptorListener listener : listeners) {
				listener.messageSent(peer);
			}
		}

		/**
		 * This method should be called when a {@link Peer} has properly
		 * received the message. All the registered
		 * {@link MessageDescriptorListener}s are then notified through
		 * {@link MessageDescriptorListener#messageReceived(Peer)}.
		 * 
		 * @param peer
		 *            the {@link Peer} confirming the reception of the message
		 */
		public void confirmReception(Peer peer) {
			for (MessageDescriptorListener listener : listeners) {
				listener.messageReceived(peer);
			}
		}
	}
}
