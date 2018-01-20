package fr.vergne.dmmorpg.sample.world;

import java.util.Arrays;

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
	
	@Override
	public String toString() {
		return Arrays.asList(x, y).toString();
	}
}
