package fr.vergne.dmmorpg.sample.world;

public interface WorldTransition {

	public WorldState getOrigin();

	public WorldAction getAction();

	public Long getTime();
}
