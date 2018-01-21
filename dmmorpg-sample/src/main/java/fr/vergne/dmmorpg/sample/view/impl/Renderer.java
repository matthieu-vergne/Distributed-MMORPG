package fr.vergne.dmmorpg.sample.view.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collection;

import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldCell;
import fr.vergne.dmmorpg.sample.world.WorldPosition;

public class Renderer {

	private static final Color NO_DATA = new Color(0, 0, 0, 0);
	private static final int CELL_WIDTH_BASE = 32;
	private static final int CELL_HEIGHT_BASE = 32;

	public void render(Graphics g, float scale, World world, WorldPosition center) {
		// TODO Check to paint efficiently:
		// http://www.oracle.com/technetwork/java/painting-140037.html
		Rectangle bounds = g.getClipBounds();

		g.setColor(NO_DATA);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		int cellWidth = (int) (CELL_WIDTH_BASE * scale);
		int cellHeight = (int) (CELL_HEIGHT_BASE * scale);
		long radiusX = (long) Math.ceil(((double) bounds.width / cellWidth - 1) / 2);
		long radiusY = (long) Math.ceil(((double) bounds.height / cellHeight - 1) / 2);
		long minWorldX = center.getX() - radiusX;
		long maxWorldX = center.getX() + radiusX;
		long minWorldY = center.getY() - radiusY;
		long maxWorldY = center.getY() + radiusY;
		int worldW = (int) ((maxWorldX - minWorldX + 1) * cellWidth);
		int worldH = (int) ((maxWorldY - minWorldY + 1) * cellHeight);
		int deltaX = (bounds.width - worldW) / 2;
		int deltaY = (bounds.height - worldH) / 2;
		for (long worldX = minWorldX; worldX <= maxWorldX; worldX++) {
			for (long worldY = minWorldY; worldY <= maxWorldY; worldY++) {
				WorldPosition worldPosition = new WorldPosition(worldX, worldY);
				WorldCell worldCell = world.getCell(worldPosition);
				int viewX = (int) (worldX - minWorldX) * cellWidth;
				int viewY = (int) (worldY - minWorldY) * cellHeight;
				int graphX = (int) (bounds.x + deltaX + viewX);
				int graphY = (int) (bounds.y + deltaY + viewY);

				Color color = getGroundColor(worldCell);
				g.setColor(color);
				g.fillRect(graphX, graphY, cellWidth, cellHeight);
				g.setColor(color.darker());
				g.drawRect(graphX, graphY, cellWidth, cellHeight);

				Collection<Player> players = worldCell.getPlayers();
				for (Player player : players) {
					player.render(g.create(graphX, graphY, cellWidth, cellHeight));
				}
			}
		}
	}

	public Color getGroundColor(WorldCell cell) {
		switch (cell.getGround()) {
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
			throw new RuntimeException("Unrendered type: " + cell.getGround());
		}
	}
}
