package fr.vergne.dmmorpg.sample.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.zone.AccessPolicy;
import fr.vergne.dmmorpg.sample.zone.Zone;

public class World implements Updatable<WorldUpdate> {

	private final Map<String, Player> players = new HashMap<>();
	private final WorldMap<Player> playerMap = new WorldMap<>();
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();
	private final Listener<WorldUpdate> updateListener = update -> Updatable.fireUpdate(listeners, update);
	private final Zone ground;
	private final Zone trees;
	private final AccessPolicy<Object> enterPolicy;
	private final AccessPolicy<Object> leavePolicy;

	public World(Zone ground, Zone trees, AccessPolicy<Object> enterPolicy, AccessPolicy<Object> leavePolicy) {
		this.playerMap.listenUpdate(updateListener);
		this.ground = ground;
		this.trees = trees;
		this.enterPolicy = enterPolicy;
		this.leavePolicy = leavePolicy;
	}

	public Player addPlayer(String id, WorldPosition position) {
		if (players.containsKey(id)) {
			throw new IllegalStateException("Player already known: " + id);
		} else {
			Player player = new Player();
			players.put(id, player);
			playerMap.put(player, position);
			player.listenUpdate(updateListener);
			return player;
		}
	}

	public void removePlayer(String id) {
		Player player = players.remove(id);
		if (player != null) {
			playerMap.remove(player);
			player.unlistenUpdate(updateListener);
		} else {
			throw new NoSuchElementException("Unknown player: " + id);
		}
	}

	public Player getPlayer(String id) {
		return players.get(id);
	}

	public boolean actTowards(String id, Direction direction) {
		Player player = getPlayer(id);
		WorldPosition start = playerMap.getPosition(player);
		Objects.requireNonNull(start, "Unknown player: " + player);
		boolean isModified = false;

		if (player.getDirection() != direction) {
			player.setDirection(direction);
			isModified = true;
		} else {
			// Direction already OK
		}

		if (canMove(player, start, direction)) {
			WorldPosition stop = start.move(direction);
			playerMap.put(player, stop);
			isModified = true;
		} else {
			// Cannot move
		}
		return isModified;
	}

	private boolean canMove(Player player, WorldPosition position, Direction direction) {
		return leavePolicy.allow(player, this, position, direction.opposite())
				&& enterPolicy.allow(player, this, position.move(direction), direction);
	}

	public WorldPosition getPosition(Player player) {
		return playerMap.getPosition(player);
	}

	public WorldCell getCell(WorldPosition position) {
		return new WorldCell(ground.getType(position), trees.getType(position), playerMap.getAllAt(position));
	}

	@Override
	public void listenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.add(listener);
	}

	@Override
	public void unlistenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.remove(listener);
	}
}
