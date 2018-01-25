package fr.vergne.dmmorpg.sample.view.impl.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import fr.vergne.dmmorpg.Renderer;

public class Filler<T> implements Renderer<T, Graphics> {

	private final Color color;

	public Filler(Color color) {
		this.color = color;
	}

	@Override
	public void render(T t, Graphics graphics) {
		Rectangle cellBounds = graphics.getClipBounds();
		graphics.setColor(color);
		graphics.fillRect(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);
		graphics.setColor(color.darker());
		graphics.drawRect(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);
	}
}
