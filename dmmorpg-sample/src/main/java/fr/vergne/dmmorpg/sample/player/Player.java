package fr.vergne.dmmorpg.sample.player;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.Property;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;

public class Player implements Updatable<WorldUpdate> {

	private final char character;
	private final Color color;
	private Direction direction = Direction.BOTTOM;
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();

	public Player(char character, Color color) {
		this.character = character;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public char getCharacter() {
		return character;
	}

	public void setDirection(Direction direction) {
		Direction old = direction;
		this.direction = direction;
		Updatable.fireUpdate(listeners, new WorldUpdate(this, Property.WORLD_DIRECTION, old, direction));
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return "" + character;
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
