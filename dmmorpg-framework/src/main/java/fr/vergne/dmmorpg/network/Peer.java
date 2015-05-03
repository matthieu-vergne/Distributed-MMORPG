package fr.vergne.dmmorpg.network;

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
	 * @return the host of the {@link Peer}
	 */
	public String getHost();

	/**
	 * 
	 * @return the port where the {@link Peer} listen
	 */
	public int getPort();
	
	/**
	 * The equal method for a {@link Peer} should return <code>true</code> when
	 * the argument is a {@link Peer} which has the same {@link PeerAddress} as
	 * returned by its {@link #getAddress()}.
	 */
	@Override
	public boolean equals(Object obj);
}
