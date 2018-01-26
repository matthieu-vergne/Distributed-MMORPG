package fr.vergne.dmmorpg.impl;

import java.util.HashMap;
import java.util.Map;

import fr.vergne.dmmorpg.Renderer;

public class ComposedRenderer<Graphics> implements Renderer<Object, Graphics> {

	private final Map<Object, Renderer<?, Graphics>> renderers = new HashMap<>();

	public <T> void put(T t, Renderer<T, Graphics> renderer) {
		renderers.put(t, renderer);
	}

	@SuppressWarnings("unchecked")
	private <T> Renderer<T, Graphics> getRenderer(T t) {
		return (Renderer<T, Graphics>) renderers.get(t);
	}

	@Override
	public void render(Object t, Graphics graphics) {
		getRenderer(t).render(t, graphics);
	}
}
