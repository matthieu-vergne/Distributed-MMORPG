package fr.vergne.dmmorpg.sample.world;

import fr.vergne.dmmorpg.sample.world.WorldCell.Type;

public class World {

	public WorldCell getCell(WorldPosition position) {
		long x = position.getX();
		long y = position.getY();
		if (x == 0 && y == 0) {
			return new WorldCell(Type.SNOW);
		} else if (-3 < x && x < 3 && -2 < y && y < 2) {
			return new WorldCell(Type.EARTH);
		} else {
			return new WorldCell(Type.WATER);
		}
	}
}
