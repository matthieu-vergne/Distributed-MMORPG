package fr.vergne.dmmorpg.network.impl;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fr.vergne.dmmorpg.network.NetworkManager.MessageListener;
import fr.vergne.dmmorpg.network.Peer;

public class SimpleNetworkManagerTest {

	private static final Properties properties = new Properties();
	private static final String propertyFile = SimpleNetworkManagerTest.class
			.getName() + ".properties";

	@BeforeClass
	public static void init() {
		try {
			properties.load(new FileInputStream(propertyFile));
		} catch (FileNotFoundException e) {
			// just ignore it, no property saved yet
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterClass
	public static void terminate() {
		try {
			properties.store(new FileOutputStream(propertyFile), null);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testSendMessageBetweenManagers() {
		final SimpleNetworkManager manager1 = createManager("manager1");
		final Peer peer1 = manager1.getCurrentPeer();

		final SimpleNetworkManager manager2 = createManager("manager2",
				manager1.getListeningPort());
		final Peer peer2 = manager2.getCurrentPeer();

		final String query1 = "How is the weather?";
		final String reply1 = "Sunny.";
		final String query2 = "What are you doing?";
		final String reply2 = "I'm replying to some weird guys.";
		final String[] receivedQueries = { null, null };
		final String[] receivedReplies = { null, null };

		manager1.register(new MessageListener() {

			@Override
			public void messageReceived(String message, Peer peer) {
				if (message.equals(query1)) {
					receivedQueries[0] = message;
					manager1.send(reply1, peer);
				} else if (message.equals(query2)) {
					receivedQueries[0] = message;
					manager1.send(reply2, peer);
				} else {
					receivedReplies[0] = message;
				}
			}
		});
		manager2.register(new MessageListener() {

			@Override
			public void messageReceived(String message, Peer peer) {
				if (message.equals(query1)) {
					receivedQueries[1] = message;
					manager2.send(reply1, peer);
				} else if (message.equals(query2)) {
					receivedQueries[1] = message;
					manager2.send(reply2, peer);
				} else {
					receivedReplies[1] = message;
				}
			}
		});

		manager1.send(query1, peer2);
		manager2.send(query2, peer1);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		assertEquals(query1, receivedQueries[1]);
		assertEquals(reply1, receivedReplies[0]);
		assertEquals(query2, receivedQueries[0]);
		assertEquals(reply2, receivedReplies[1]);
	}

	@Test
	public void testPortBusyAfterInstantiation() throws IOException {
		SimpleNetworkManager manager = createManager("managerBusy");
		ServerSocket socket = new ServerSocket();
		socket.setReuseAddress(true);
		try {
			socket.bind(new InetSocketAddress(manager.getListeningPort()));
			fail("No exception thrown");
		} catch (IOException e) {
		}
	}

	@Test
	public void testPortFreeOnStop() throws IOException {
		SimpleNetworkManager manager = createManager("managerStop");
		int port = manager.getListeningPort();
		manager.stop();

		ServerSocket socket = new ServerSocket();
		socket.setReuseAddress(true);
		socket.bind(new InetSocketAddress(port));
	}

	@Ignore("Not a critical requirement, so ignore unless one find a way to satisfy it.")
	@Test
	public void testAutoFreePortIfManagerUnused() throws IOException {
		SimpleNetworkManager manager = createManager("managerFinalization");
		int port = manager.getListeningPort();
		manager = null;

		long start = System.currentTimeMillis();
		do {
			System.runFinalization();
			System.gc();
		} while (System.currentTimeMillis() - start < 100);

		ServerSocket socket = new ServerSocket();
		socket.setReuseAddress(true);
		try {
			socket.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			fail("Port " + port + " still busy");
		}
	}

	private SimpleNetworkManager createManager(String propertyKey,
			int... forbiddenPorts) {
		SimpleNetworkManager manager = null;
		while (manager == null) {
			String property = properties.getProperty(propertyKey);
			int port;
			if (property == null) {
				port = new Random().nextInt(65536 - 1024) + 1024;
				properties.setProperty(propertyKey, "" + port);
			} else {
				port = Integer.parseInt(property);
			}

			try {
				manager = new SimpleNetworkManager(port);
			} catch (IllegalArgumentException e) {
				String forbidden;
				if (forbiddenPorts.length == 0) {
					forbidden = "";
				} else {
					forbidden = " (excepted";
					for (int number : forbiddenPorts) {
						forbidden += " " + number;
					}
					forbidden += ")";
				}
				String answer = JOptionPane.showInputDialog("The port " + port
						+ " seems busy, please choose another port" + forbidden
						+ ":");
				if (answer == null || answer.isEmpty()) {
					fail("No port provided to run the network manager.");
				} else {
					properties.setProperty(propertyKey, answer);
				}
			}
		}
		return manager;
	}

}
