package fr.vergne.dmmorpg.sample.world;

import java.util.Collection;

import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.zone.Zone.Descriptor;

public class WorldCell {

	private final Descriptor descriptor;
	private final Collection<Player> players;

	public WorldCell(Descriptor descriptor, Collection<Player> players) {
		this.descriptor = descriptor;
		this.players = players;
	}

	public Descriptor getZoneDescriptor() {
		return descriptor;
	}

	public Collection<Player> getPlayers() {
		return players;
	}
}
