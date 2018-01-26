package fr.vergne.dmmorpg.sample.zone;

import fr.vergne.dmmorpg.sample.world.WorldPosition;

public interface Zone {
	public Type getType(WorldPosition position);

	public static class Type {
	}
}
