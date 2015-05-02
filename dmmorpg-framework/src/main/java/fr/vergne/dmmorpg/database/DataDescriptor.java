package fr.vergne.dmmorpg.database;

import fr.vergne.dmmorpg.network.Peer;

/**
 * A {@link DataDescriptor} centralize all the information of a single piece of
 * data. In particular, it provides the {@link DataReader} and
 * {@link DataWriter} to access the data values, but also the required
 * information regarding its storage.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public interface DataDescriptor<T> {

	/**
	 * 
	 * @return the {@link DataReader} access on the data
	 */
	public DataReader<T> getReadAccess();

	/**
	 * 
	 * @return the {@link DataWriter} access on the data
	 */
	public DataWriter<T> getWriteAccess();

	/**
	 * 
	 * @return <code>true</code> if the data is available locally or should be
	 *         retrieved from another {@link Peer}.
	 */
	public boolean isLocal();

	/**
	 * 
	 * @return the {@link Peer} in charge of the data
	 */
	public Peer getOwner();
}
