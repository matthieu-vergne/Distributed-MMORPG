package fr.vergne.dmmorpg.sample.world;

import fr.vergne.dmmorpg.sample.zone.AccessPolicy;
import fr.vergne.dmmorpg.sample.zone.Zone;

public class WorldBuilder {

	private Zone ground;
	private Zone trees;
	private AccessPolicy<Object> enterPolicy;
	private AccessPolicy<Object> leavePolicy;

	public void setGround(Zone ground) {
		this.ground = ground;
	}

	public void setTrees(Zone trees) {
		this.trees = trees;
	}

	public void setEnterAccessPolicy(AccessPolicy<Object> accessPolicy) {
		this.enterPolicy = accessPolicy;
	}

	public void setLeaveAccessPolicy(AccessPolicy<Object> accessPolicy) {
		this.leavePolicy = accessPolicy;
	}

	public World build() {
		return new World(ground, trees, enterPolicy, leavePolicy);
	}
}
