package fr.vergne.dmmorpg.database;

/**
 * This interface is the basic interface of the underlying architecture which
 * stores all the data in a decentralized way.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Key>
 *            The type of key to use to identify the data
 * @deprecated use {@link DataManager} instead
 */
@Deprecated
public interface Database<Key> {

	/**
	 * 
	 * @param key
	 *            the data identifier
	 * @return the value stored for the given key
	 * @throws UnknownKeyException
	 *             if no such key is found in the {@link Database}
	 */
	public Object getData(Key key) throws UnknownKeyException;

	/**
	 * 
	 * @param key
	 *            the data identifier
	 * @return <code>true</code> if such a key exists in the {@link Database},
	 *         <code>false</code> otherwise
	 */
	public boolean hasData(Key key);

	/**
	 * 
	 * @param key
	 *            the data identifier
	 * @param value
	 *            the value to store in the {@link Database} with the given key
	 * @throws IncompatibleValueException
	 *             if the value provided does not correspond to data storable
	 *             with this key
	 */
	public void setData(Key key, Object value)
			throws IncompatibleValueException;

	/**
	 * This method allows to be notified when an update in the {@link Database}
	 * will occur.
	 * 
	 * @param listener
	 *            the listener which will be notified of the updates
	 */
	public void registerUpdateListener(UpdateListener<Key> listener);

	/**
	 * Stop the notification process of a given listener.
	 * 
	 * @param listener
	 *            the listener which should not be notified anymore of the
	 *            updates
	 */
	public void removeUpdateListener(UpdateListener<Key> listener);

	/**
	 * An {@link UpdateListener} aims at reacting to {@link Database} updates as
	 * soon as they occur. Each {@link UpdateListener} should be notified when
	 * an update occurs in the {@link Database}.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <Key>
	 *            The type of key used by the {@link Database}
	 */
	public static interface UpdateListener<Key> {
		/**
		 * The source of an update event.
		 * 
		 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
		 * 
		 */
		// TODO prefer a description allowing to identify the exact machine
		public static enum Source {
			/**
			 * The update has been requested by the local machine.
			 */
			Local,
			/**
			 * The update has been requested by a remote machine.
			 */
			Remote
		}

		/**
		 * A new value has been stored in the {@link Database}.
		 * 
		 * @param source
		 *            the requester of the adding
		 * @param newKey
		 *            the key of the new entry
		 * @param newValue
		 *            the value of the new entry
		 */
		public void add(Source source, Key newKey, Object newValue);

		/**
		 * An existing value in the {@link Database} has been modified.
		 * 
		 * @param source
		 *            the requester of the update
		 * @param key
		 *            the key of the modified entry
		 * @param oldValue
		 *            the previous value of the entry
		 * @param newValue
		 *            the current value of the entry
		 */
		public void modify(Source source, Key key, Object oldValue,
				Object newValue);

		/**
		 * A value has been removed from the {@link Database}.
		 * 
		 * @param source
		 *            the requester of the removal
		 * @param oldKey
		 *            the key of the removed entry
		 * @param oldValue
		 *            the value of the removed entry
		 */
		public void remove(Source source, Key oldKey, Object oldValue);
	}

	/**
	 * This exception signals the use of a {@link Database}'s key which was not
	 * used before (no value has been stored with this key) while it should have
	 * been used.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 */
	@SuppressWarnings("serial")
	public class UnknownKeyException extends RuntimeException {
		public <Key> UnknownKeyException(Key key) {
			this(key, null);
		}

		public <Key> UnknownKeyException(Key key, Throwable cause) {
			super("No stored value for the key " + key, cause);
		}
	}

	/**
	 * This exception signals the use of a value for a {@link Database}'s key
	 * which does not correspond to the storage of such value.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 */
	@SuppressWarnings("serial")
	public class IncompatibleValueException extends RuntimeException {
		public <Key> IncompatibleValueException(Key key, Object value) {
			this(key, value, null);
		}

		public <Key> IncompatibleValueException(Key key, Object value,
				Throwable cause) {
			super("Incompatible value " + value + " for the key " + key, cause);
		}
	}
}
