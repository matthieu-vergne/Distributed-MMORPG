package fr.vergne.dmmorpg.sample.view.impl;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.vergne.dmmorpg.View;
import fr.vergne.dmmorpg.impl.FinalLink;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldCell;
import fr.vergne.dmmorpg.sample.world.WorldPosition;

public class CenteredView implements View<World, WorldCell, Graphics, Rectangle> {

	private final WorldPosition center;
	private final Scaler scaler;

	public CenteredView(WorldPosition center, Scaler scaler) {
		this.center = center;
		this.scaler = scaler;
	}

	@Override
	public Iterator<Link<WorldCell, Rectangle>> map(World world, Graphics graphics) {
		Rectangle bounds = graphics.getClipBounds();
		int cellWidth = scaler.getCellWidth();
		int cellHeight = scaler.getCellHeight();
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
		return new Iterator<View.Link<WorldCell, Rectangle>>() {
			long worldX = minWorldX;
			long worldY = minWorldY;

			@Override
			public boolean hasNext() {
				return worldY <= maxWorldY;
			}

			@Override
			public Link<WorldCell, Rectangle> next() {
				WorldPosition worldPosition = new WorldPosition(worldX, worldY);
				WorldCell worldCell = world.getCell(worldPosition);

				int viewX = (int) (worldX - minWorldX) * cellWidth;
				int viewY = (int) (worldY - minWorldY) * cellHeight;
				int graphX = (int) (bounds.x + deltaX + viewX);
				int graphY = (int) (bounds.y + deltaY + viewY);
				Rectangle rectangle = new Rectangle(graphX, graphY, cellWidth, cellHeight);

				passToNext();
				return new FinalLink<>(worldCell, rectangle);
			}

			private void passToNext() {
				worldX++;
				if (worldX > maxWorldX) {
					worldX = minWorldX;
					worldY++;
				} else {
					// Still same row
				}
			}
		};
	}

	@Override
	public String toString() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("center", center);
		data.put("scaler", scaler);
		return "View" + data;
	}
}
