package fr.vergne.dmmorpg.database;

/**
 * A {@link DataWriter} provides the ability to modify a piece of data.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public interface DataWriter<T> {

	public void set(T newValue);
}
