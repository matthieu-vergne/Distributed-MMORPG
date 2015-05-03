package fr.vergne.dmmorpg.network;

import java.util.Collection;

/**
 * A {@link MessageDescriptor} provides a description of the status of a message
 * sent. In particular, it allows to know when the message it describes is sent
 * or received and by which {@link Peer}s.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface MessageDescriptor {
	/**
	 * 
	 * @return the message sent
	 */
	public String getMessage();

	/**
	 * 
	 * @return the {@link Peer}s which should receive this message
	 */
	public Collection<Peer> getTargets();

	/**
	 * 
	 * @return the {@link Peer}s to which the message has been sent so far
	 */
	public Collection<Peer> getSentTargets();

	/**
	 * 
	 * @return the {@link Peer}s which have received the message so far
	 */
	public Collection<Peer> getReceivedTargets();

	/**
	 * 
	 * @param listener
	 *            the {@link MessageDescriptorListener} to register in order to
	 *            be notified when this {@link MessageDescriptor} changes
	 */
	public void register(MessageDescriptorListener listener);

	/**
	 * 
	 * @param listener
	 *            a registered {@link MessageDescriptorListener} which should
	 *            not be used anymore
	 */
	public void unregister(MessageDescriptorListener listener);

	/**
	 * A {@link MessageDescriptorListener} allows to be notified when a specific
	 * status of a {@link MessageDescriptor} changes.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 */
	public interface MessageDescriptorListener {
		/**
		 * This method is called when the message has been sent to a specific
		 * {@link Peer}. However, it does not guarantee that the {@link Peer}
		 * has received it. Check the {@link #messageReceived(Peer)} method to
		 * be notified about the proper reception of the message.
		 * 
		 * @param peer
		 *            the {@link Peer} to which the message has been sent
		 */
		public void messageSent(Peer peer);

		/**
		 * This method is called when a {@link Peer} acknowledge the reception
		 * of the message.
		 * 
		 * @param peer
		 *            the {@link Peer} which has received the message
		 */
		public void messageReceived(Peer peer);
	}
}
