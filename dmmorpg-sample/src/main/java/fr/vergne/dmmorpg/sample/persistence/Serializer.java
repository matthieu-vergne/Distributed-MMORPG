package fr.vergne.dmmorpg.sample.persistence;

public interface Serializer<T> {

	public void save(T object);
}
