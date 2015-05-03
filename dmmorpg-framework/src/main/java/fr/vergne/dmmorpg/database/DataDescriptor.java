package fr.vergne.dmmorpg.database;

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
}
