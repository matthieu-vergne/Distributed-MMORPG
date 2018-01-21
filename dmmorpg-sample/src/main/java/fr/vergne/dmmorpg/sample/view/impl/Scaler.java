package fr.vergne.dmmorpg.sample.view.impl;

import java.util.Collection;
import java.util.LinkedList;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.Property;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;

public class Scaler implements Updatable<WorldUpdate> {
	private float scale = 1.0f;
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();

	public void increaseScale() {
		rescale(2.0f);
	}

	public void decreaseScale() {
		rescale(0.5f);
	}

	private void rescale(float factor) {
		float oldValue = scale;
		scale = Math.max(Math.min(scale * factor, 8), 1);
		float newValue = scale;
		if (oldValue != newValue) {
			Updatable.fireUpdate(listeners, new WorldUpdate(null, Property.VIEW_SCALE, oldValue, newValue));
		} else {
			// No change
		}
	}

	public float getScale() {
		return scale;
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
