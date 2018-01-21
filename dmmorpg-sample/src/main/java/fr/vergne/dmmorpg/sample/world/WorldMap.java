package fr.vergne.dmmorpg.sample.world;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.Property;
import fr.vergne.dmmorpg.sample.world.WorldMap.PositionUpdate;

public class WorldMap<T> implements Updatable<PositionUpdate<T>> {

	private final Map<T, WorldPosition> objects = new HashMap<>();
	private final Map<WorldPosition, Collection<T>> positions = new HashMap<>();
	private final List<Listener<? super PositionUpdate<T>>> listeners = new LinkedList<>();

	public void put(T object, WorldPosition newPosition) {
		Objects.requireNonNull(object, "No object provided");
		Objects.requireNonNull(newPosition, "No position provided");

		WorldPosition currentPosition = objects.get(object);
		if (newPosition.equals(currentPosition)) {
			// Irrelevant call
		} else {
			if (currentPosition == null) {
				// Unknown object
			} else {
				Collection<T> p = positions.get(currentPosition);
				p.remove(object);
				if (p.isEmpty()) {
					positions.remove(currentPosition);
				} else {
					// Not empty yet
				}
			}

			objects.put(object, newPosition);
			Collection<T> p = positions.get(newPosition);
			if (p == null) {
				p = new LinkedList<>();
				positions.put(newPosition, p);
			} else {
				// Reuse
			}
			p.add(object);

			Updatable.fireUpdate(listeners, new PositionUpdate<>(object, currentPosition, newPosition));
		}
	}

	public boolean remove(T object) {
		WorldPosition position = objects.remove(object);
		if (position == null) {
			return false;
		} else {
			positions.get(position).remove(object);
			return true;
		}
	}

	public WorldPosition getPosition(T object) {
		return objects.get(object);
	}

	public Collection<T> getAllAt(WorldPosition position) {
		Collection<T> objects = positions.get(position);
		if (objects == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableCollection(objects);
		}
	}

	public static class PositionUpdate<T> extends WorldUpdate {
		public PositionUpdate(T object, WorldPosition oldPosition, WorldPosition newPosition) {
			super(object, Property.WORLD_POSITION, oldPosition, newPosition);
		}
	}

	@Override
	public void listenUpdate(Listener<? super PositionUpdate<T>> listener) {
		listeners.add(listener);
	}

	@Override
	public void unlistenUpdate(Listener<? super PositionUpdate<T>> listener) {
		listeners.remove(listener);
	}
}
