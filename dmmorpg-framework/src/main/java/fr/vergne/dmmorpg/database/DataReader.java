package fr.vergne.dmmorpg.database;

/**
 * A {@link DataReader} provides the read access to a specific piece of data.
 * The value of the data can be retrieved on demand through {@link #get()}
 * (synchronous) or on update by registering to {@link #register(DataListener)}
 * (asynchronous).
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public interface DataReader<T> {

	/**
	 * 
	 * @return the current value of the data
	 */
	public T get();

	/**
	 * 
	 * @param listener
	 *            the {@link DataListener} which will be notified when the data
	 *            changes
	 */
	public void register(DataListener<T> listener);

	/**
	 * 
	 * @param listener
	 *            a previously registered {@link DataListener} which should not
	 *            be used anymore
	 */
	public void unregister(DataListener<T> listener);

	/**
	 * A {@link DataListener} aims at reacting to the change of a piece of data
	 * managed bya {@link DataReader}. When the data changes, the new value is
	 * provided to {@link #valueUpdated(Object)}.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <T>
	 */
	public interface DataListener<T> {
		/**
		 * 
		 * @param newValue
		 *            the new value of the data
		 */
		public void valueUpdated(T newValue);
	}
}
