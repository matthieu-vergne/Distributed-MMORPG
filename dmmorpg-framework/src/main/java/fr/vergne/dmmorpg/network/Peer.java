package fr.vergne.dmmorpg.network;

import java.net.URL;

/**
 * A {@link Peer} identifies a client on the network. One machine can host
 * several {@link Peer}s at once, although it is more usual to have only one
 * {@link Peer} per machine.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface Peer {

	/**
	 * 
	 * @return the {@link URL} (location) of the {@link Peer}
	 */
	public URL getURL();

	/**
	 * 
	 * @return <code>true</code> if this {@link Peer} is hosted by the local
	 *         machine, <code>false</code> otherwise.
	 */
	public boolean isLocal();

	/**
	 * 
	 * @return <code>true</code> if we can interact with the {@link Peer}, false
	 *         otherwise.
	 */
	public boolean isVisible();

	/**
	 * This method is a round-trip communication. It wait until the {@link Peer}
	 * replies to the message. If you don't want to wait for the reply, use
	 * {@link #send(String)}.
	 * 
	 * @param message
	 *            the message to send to the {@link Peer}
	 * @return the answer of the {@link Peer}
	 */
	public String request(String message);

	/**
	 * This method is a one-way communication. It returns when the message has
	 * been sent. If you want to wait for the reply of the {@link Peer}, use
	 * {@link #request(String)}. Any message received from this {@link Peer}
	 * will be notified to the {@link MessageListener}s registered with
	 * {@link #register(MessageListener)}.
	 * 
	 * @param message
	 *            the message to send to the {@link Peer}
	 */
	public void send(String message);

	/**
	 * Register a {@link MessageListener} to react upon reception of a message
	 * from this {@link Peer}. Notice that replies to {@link #request(String)}
	 * are not managed.
	 * 
	 * @param listener
	 *            the {@link MessageListener} describing how to react to the
	 *            messages received from this {@link Peer}.
	 */
	public void register(MessageListener listener);

	/**
	 * 
	 * @param listener
	 *            the registered {@link MessageListener} to remove
	 */
	public void unregister(MessageListener listener);

	/**
	 * A {@link MessageListener} allows to react immediately to the reception of
	 * a message sent by a {@link Peer}.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 */
	public interface MessageListener {
		/**
		 * 
		 * @param message
		 *            the message sent by the {@link Peer}
		 */
		public void messageReceived(String message);
	}
}
