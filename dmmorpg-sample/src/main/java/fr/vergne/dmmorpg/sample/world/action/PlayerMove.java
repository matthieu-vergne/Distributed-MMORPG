package fr.vergne.dmmorpg.sample.world.action;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldAction;
import fr.vergne.dmmorpg.sample.world.WorldState;

public class PlayerMove implements WorldAction {

	private final String playerID;
	private final Direction direction;

	public PlayerMove(String playerID, Direction direction) {
		this.playerID = playerID;
		this.direction = direction;
	}

	@Override
	public WorldState apply(WorldState instant) {
		return () -> {
			World world = instant.create();
			world.actTowards(playerID, direction);
			return world;
		};
	}

	@Override
	public String toString() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("type", getClass().getSimpleName());
		data.put("id", playerID);
		data.put("direction", direction);
		return data.toString();
	}
}
