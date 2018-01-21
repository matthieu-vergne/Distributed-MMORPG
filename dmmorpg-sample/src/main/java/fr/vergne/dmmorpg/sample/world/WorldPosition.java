package fr.vergne.dmmorpg.sample.world;

import java.util.Arrays;

import fr.vergne.dmmorpg.sample.Direction;

public class WorldPosition {

	private final long x;
	private final long y;

	public WorldPosition(long x, long y) {
		this.x = x;
		this.y = y;
	}

	public long getX() {
		return x;
	}

	public long getY() {
		return y;
	}

	public WorldPosition move(Direction direction) {
		switch (direction) {
		case BOTTOM:
			return new WorldPosition(x, y + 1);
		case TOP:
			return new WorldPosition(x, y - 1);
		case LEFT:
			return new WorldPosition(x - 1, y);
		case RIGHT:
			return new WorldPosition(x + 1, y);
		default:
			throw new IllegalArgumentException("Umanaged direction: " + this);
		}
	}

	@Override
	public String toString() {
		return Arrays.asList(x, y).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof WorldPosition) {
			WorldPosition p = (WorldPosition) obj;
			return p.x == x && p.y == y;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int) ((Math.abs(x) + 1) * (Math.abs(y) + 1));
	}
}
