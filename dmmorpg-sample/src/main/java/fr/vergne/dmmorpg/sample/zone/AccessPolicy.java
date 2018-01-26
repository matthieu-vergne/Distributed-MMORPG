package fr.vergne.dmmorpg.sample.zone;

import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.world.WorldPosition;

public interface AccessPolicy<T> {

	public boolean allow(T t, WorldPosition position, Direction direction);

	public static AccessPolicy<Object> ALLOW_ALL = (t, p, d) -> true;
	public static AccessPolicy<Object> BLOCK_ALL = (t, p, d) -> false;
}
