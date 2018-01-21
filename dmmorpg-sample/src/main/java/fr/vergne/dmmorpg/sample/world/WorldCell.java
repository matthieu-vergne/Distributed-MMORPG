package fr.vergne.dmmorpg.sample.world;

import java.util.Collection;

import fr.vergne.dmmorpg.sample.player.Player;

public class WorldCell {

	private final Ground ground;
	private final Collection<Player> players;
	private final AccessPolicy<? super Player> playerAccessPolicy;

	public WorldCell(Ground ground, Collection<Player> players, AccessPolicy<? super Player> playerAccessPolicy) {
		this.ground = ground;
		this.players = players;
		this.playerAccessPolicy = playerAccessPolicy;
	}

	public Ground getGround() {
		return ground;
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public AccessPolicy<? super Player> getPlayerAccessPolicy() {
		return playerAccessPolicy;
	}
}
