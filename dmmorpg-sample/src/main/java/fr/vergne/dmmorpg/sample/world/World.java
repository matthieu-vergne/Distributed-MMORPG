package fr.vergne.dmmorpg.sample.world;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.player.Player;

public class World implements Updatable<WorldUpdate> {

	private final WorldMap<Player> playerMap = new WorldMap<>();
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();
	private final Listener<WorldUpdate> updateListener = update -> Updatable.fireUpdate(listeners, update);

	public World() {
		playerMap.listenUpdate(updateListener);
	}

	public void add(Player player, WorldPosition position) {
		if (playerMap.getPosition(player) == null) {
			playerMap.put(player, position);
			player.listenUpdate(updateListener);
		} else {
			throw new IllegalStateException("Player already known: " + player);
		}
	}

	public void remove(Player player) {
		if (playerMap.remove(player)) {
			player.unlistenUpdate(updateListener);
		} else {
			// No such thing
		}
	}

	public void actTowards(Player player, Direction direction) {
		WorldPosition start = playerMap.getPosition(player);
		Objects.requireNonNull(start, "Unknown player: " + player);

		if (player.getDirection() != direction) {
			player.setDirection(direction);
		} else {
			// Direction already OK
		}

		if (canMove(player, start, direction)) {
			WorldPosition stop = start.move(direction);
			playerMap.put(player, stop);
		} else {
			// Cannot move
		}
	}

	private boolean canMove(Player player, WorldPosition position, Direction direction) {
		WorldCell c1 = getCell(position);
		WorldCell c2 = getCell(position.move(direction));
		return c1.getPlayerAccessPolicy().canLeave(player, direction.opposite())
				&& c2.getPlayerAccessPolicy().canEnter(player, direction);
	}

	public WorldPosition getPosition(Player player) {
		return playerMap.getPosition(player);
	}

	public WorldCell getCell(WorldPosition position) {
		long x = position.getX();
		long y = position.getY();
		Collection<Player> players = playerMap.getAllAt(position);
		if (-3 < x && x < 3 && -2 < y && y < 2) {
			return new WorldCell(Ground.SNOW, players, AccessPolicy.ALLOW_ALL);
		} else if (-10 < x && x < 10 && -10 < y && y < 10) {
			return new WorldCell(Ground.EARTH, players, AccessPolicy.ALLOW_ALL);
		} else {
			return new WorldCell(Ground.WATER, players, AccessPolicy.BLOCK_ALL);
		}
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
