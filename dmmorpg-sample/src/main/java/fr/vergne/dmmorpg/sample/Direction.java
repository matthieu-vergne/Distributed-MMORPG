package fr.vergne.dmmorpg.sample;

public enum Direction {
	TOP, BOTTOM, LEFT, RIGHT;

	public Direction opposite() {
		switch (this) {
		case BOTTOM:
			return TOP;
		case TOP:
			return BOTTOM;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		default:
			throw new IllegalArgumentException("Umanaged direction: " + this);
		}
	}
}
