package fr.vergne.dmmorpg.database;

public interface DataManager {

	public <T> void addData(Object key, DataDescriptor<T> descriptor);

	public <T> DataDescriptor<T> getData(Object key);
}
