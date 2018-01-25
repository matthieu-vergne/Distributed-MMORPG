package fr.vergne.dmmorpg.sample.player;

import java.util.Collection;
import java.util.LinkedList;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.Property;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;

public class Player implements Updatable<WorldUpdate> {

	private Direction direction = Direction.BOTTOM;
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();

	public void setDirection(Direction direction) {
		Direction old = direction;
		this.direction = direction;
		Updatable.fireUpdate(listeners, new WorldUpdate(this, Property.WORLD_DIRECTION, old, direction));
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public void listenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.add(listener);
	}

	@Override
	public void unlistenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.remove(listener);
	}
}
