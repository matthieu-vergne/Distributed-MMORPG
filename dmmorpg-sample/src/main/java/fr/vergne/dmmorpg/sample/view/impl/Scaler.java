package fr.vergne.dmmorpg.sample.view.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;
import fr.vergne.dmmorpg.sample.world.action.Property;

public class Scaler implements Updatable<WorldUpdate> {
	private final int minWidth;
	private final int minHeight;
	private int scale = 1;
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();

	public Scaler(int minWidth, int minHeight, int scale) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.scale = scale;
	}

	public Scaler(int minWidth, int minHeight) {
		this(minWidth, minHeight, 1);
	}

	public void increaseScale() {
		rescale(2.0f);
	}

	public void decreaseScale() {
		rescale(0.5f);
	}

	private void rescale(float factor) {
		float oldValue = scale;
		scale = (int) Math.max(Math.min(scale * factor, 8), 1);
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

	public int getCellWidth() {
		return minWidth * scale;
	}

	public int getCellHeight() {
		return minHeight * scale;
	}

	@Override
	public void listenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.add(listener);
	}

	@Override
	public void unlistenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.remove(listener);
	}

	@Override
	public String toString() {
		Map<String, Object> data = new HashMap<>();
		data.put("cell width", getCellWidth());
		data.put("cell height", getCellHeight());
		data.put("min width", minWidth);
		data.put("min height", minHeight);
		data.put("scale", scale);
		return data.toString();
	}
}
