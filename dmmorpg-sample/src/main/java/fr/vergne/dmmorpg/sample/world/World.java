package fr.vergne.dmmorpg.sample.world;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.zone.AccessPolicy;
import fr.vergne.dmmorpg.sample.zone.Zone;

public class World implements Updatable<WorldUpdate> {

	private final WorldMap<Player> playerMap = new WorldMap<>();
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();
	private final Listener<WorldUpdate> updateListener = update -> Updatable.fireUpdate(listeners, update);
	private Zone ground;
	private AccessPolicy<Object> enterPolicy;
	private AccessPolicy<Object> leavePolicy;

	public World() {
		playerMap.listenUpdate(updateListener);
	}

	public void setGround(Zone ground) {
		this.ground = ground;
	}

	public void setEnterAccessPolicy(AccessPolicy<Object> accessPolicy) {
		this.enterPolicy = accessPolicy;
	}

	public void setLeaveAccessPolicy(AccessPolicy<Object> accessPolicy) {
		this.leavePolicy = accessPolicy;
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
		return leavePolicy.allow(player, position, direction.opposite())
				&& enterPolicy.allow(player, position.move(direction), direction);
	}

	public WorldPosition getPosition(Player player) {
		return playerMap.getPosition(player);
	}

	public WorldCell getCell(WorldPosition position) {
		return new WorldCell(ground.getType(position), playerMap.getAllAt(position));
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
