package fr.vergne.dmmorpg.impl;

import java.util.HashMap;
import java.util.Map;

import fr.vergne.dmmorpg.Renderer;

public class RendererComposer<Graphics> {

	private final Map<Object, Renderer<?, Graphics>> renderers = new HashMap<>();

	public <T> void set(T t, Renderer<T, Graphics> renderer) {
		renderers.put(t, renderer);
	}

	public Renderer<Object, Graphics> build() {
		Map<Object, Renderer<?, Graphics>> renderers = new HashMap<>(this.renderers);
		return new Renderer<Object, Graphics>() {

			@Override
			public void render(Object t, Graphics graphics) {
				@SuppressWarnings("unchecked")
				Renderer<Object, Graphics> renderer = (Renderer<Object, Graphics>) renderers.get(t);
				renderer.render(t, graphics);
			}
		};
	}

}
