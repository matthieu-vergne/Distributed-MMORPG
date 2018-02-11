package fr.vergne.dmmorpg.sample.world.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.vergne.dmmorpg.sample.world.WorldAction;
import fr.vergne.dmmorpg.sample.world.WorldState;
import fr.vergne.dmmorpg.sample.world.WorldTransition;

public class FinalWorldTransition implements WorldTransition {
	
	private final long time;
	private final WorldState origin;
	private final WorldAction action;

	public FinalWorldTransition(long time, WorldState origin, WorldAction action) {
		this.time = time;
		this.origin = origin;
		this.action = action;
	}

	@Override
	public WorldState getOrigin() {
		return origin;
	}

	@Override
	public WorldAction getAction() {
		return action;
	}

	@Override
	public Long getTime() {
		return time;
	}

	@Override
	public String toString() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("time", getTime());
		data.put("origin", getOrigin());
		data.put("action", getAction());
		return data.toString();
	}
}
