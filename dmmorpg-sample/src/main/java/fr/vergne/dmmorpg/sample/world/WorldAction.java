package fr.vergne.dmmorpg.sample.world;

import fr.vergne.dmmorpg.Action;

public interface WorldAction extends Action<WorldState> {
	@Override
	public WorldState apply(WorldState state);
}
