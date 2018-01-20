package fr.vergne.dmmorpg.sample.world;

// TODO Not "cell", find a different term
public class WorldCell {

	public static enum Type {
		EMPTY, WATER, EARTH, HERB, SNOW
	}

	private final Type type;

	public WorldCell(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
