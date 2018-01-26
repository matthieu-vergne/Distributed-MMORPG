package fr.vergne.dmmorpg.sample.world;

import java.util.Collection;

import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.zone.Zone;

public class WorldCell {

	private final Zone.Type ground;
	private final Collection<Player> players;

	public WorldCell(Zone.Type ground, Collection<Player> players) {
		this.ground = ground;
		this.players = players;
	}

	public Zone.Type getGround() {
		return ground;
	}

	public Collection<Player> getPlayers() {
		return players;
	}
}
