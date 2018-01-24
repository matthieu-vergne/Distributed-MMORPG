package fr.vergne.dmmorpg.sample.view.impl.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import fr.vergne.dmmorpg.Renderer;
import fr.vergne.dmmorpg.sample.world.Ground;

public class GroundRenderer implements Renderer<Ground, Graphics> {

	@Override
	public void render(Ground ground, Graphics graphics) {
		Rectangle cellBounds = graphics.getClipBounds();
		Color color = getGroundColor(ground);
		graphics.setColor(color);
		graphics.fillRect(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);
		graphics.setColor(color.darker());
		graphics.drawRect(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);
	}

	public Color getGroundColor(Ground ground) {
		switch (ground) {
		case EMPTY:
			return Color.BLACK;
		case WATER:
			return Color.BLUE;
		case EARTH:
			return Color.ORANGE;
		case HERB:
			return Color.GREEN;
		case SNOW:
			return Color.WHITE;
		default:
			throw new RuntimeException("Unrendered type: " + ground);
		}
	}
}
