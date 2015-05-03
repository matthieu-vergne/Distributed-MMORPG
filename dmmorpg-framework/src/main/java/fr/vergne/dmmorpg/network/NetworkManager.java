package fr.vergne.dmmorpg.network;

/**
 * A {@link NetworkManager} provides all the basics for dealing with network
 * communications.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface NetworkManager {

	/**
	 * 
	 * @return the {@link Peer} behind the {@link NetworkManager}
	 */
	public Peer getCurrentPeer();

	/**
	 * This method allows to send a message to one or more {@link Peer}s. It is
	 * not blocking and returns once the {@link NetworkManager} takes in charge
	 * the message. This means that it can return even before to send anything,
	 * thus minimizing the waiting time. In order to deal with this asynchronous
	 * management, a {@link MessageDescriptor} is returned so that the calling
	 * method can choose how to continue its execution.
	 * 
	 * @param message
	 *            the message to send
	 * @param peers
	 *            the {@link Peer}s to send this message to
	 * @return a {@link MessageDescriptor} providing information on the status
	 *         of the message
	 */
	public MessageDescriptor send(String message, Peer... peers);
	
	/**
	 * One can register a {@link MessageListener} through this method to be
	 * notified when a message is received from another {@link Peer}.
	 * 
	 * @param listener
	 *            the {@link MessageListener} to register
	 */
	public void register(MessageListener listener);

	/**
	 * 
	 * @param listener
	 *            a registered {@link MessageListener} which should not be used
	 *            anymore
	 */
	public void unregister(MessageListener listener);

	/**
	 * A {@link MessageListener} allows to deal with the reception of messages
	 * coming from other {@link Peer}s.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 */
	public interface MessageListener {
		/**
		 * 
		 * @param message
		 *            the message received
		 * @param peer
		 *            the {@link Peer} which has sent this message
		 */
		public void messageReceived(String message, Peer peer);
	}
}
