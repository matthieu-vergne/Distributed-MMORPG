package fr.vergne.dmmorpg.sample.persistence;

public interface Deserializer<T> {

	public T load();
}
