package fr.vergne.dmmorpg.sample.view.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.vergne.dmmorpg.sample.view.View;
import fr.vergne.dmmorpg.sample.view.ViewPosition;
import fr.vergne.dmmorpg.sample.view.impl.PositionedView.ViewCell;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldCell;
import fr.vergne.dmmorpg.sample.world.WorldPosition;

public class PositionedView implements View<Graphics>, Iterable<ViewCell> {

	private static final Color NO_DATA = new Color(0, 0, 0, 0);
	private static final int CELL_WIDTH = 32;
	private static final int CELL_HEIGHT = 32;

	private final World world;
	private final WorldPosition worldOrigin;
	private final int width;
	private final int height;
	private final ViewPosition focus;

	public PositionedView(World world, WorldPosition center, int radius) {
		this(world, new WorldPosition(center.getX() - radius, center.getY() - radius), 2 * radius + 1, 2 * radius + 1,
				new ViewPosition(radius, radius));
	}

	public PositionedView(World world, WorldPosition topLeft, int width, int height, ViewPosition focus) {
		this.world = world;
		this.worldOrigin = topLeft;

		this.width = width;
		this.height = height;
		this.focus = focus;
	}

	@Override
	public String toString() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("world", world);
		data.put("worldOrigin", worldOrigin);
		data.put("width", width);
		data.put("height", height);
		data.put("focus", focus);
		return "View" + data;
	}

	@Override
	public void render(Graphics g) {
		// TODO Check to paint efficiently:
		// http://www.oracle.com/technetwork/java/painting-140037.html
		Rectangle bounds = g.getClipBounds();

		g.setColor(NO_DATA);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		int viewWidth = this.width * CELL_WIDTH;
		int viewHeight = this.height * CELL_HEIGHT;
		int deltaX = (bounds.width - viewWidth) / 2;
		int deltaY = (bounds.height - viewHeight) / 2;

		for (ViewCell cell : this) {
			ViewPosition cellPosition = cell.getPosition();
			ViewPosition cellOrigin = cell.getOrigin();
			int graphX = (int) (cellPosition.getX() - cellOrigin.getX() + deltaX);
			int graphY = (int) (cellPosition.getY() - cellOrigin.getY() + deltaY);

			if (graphX > bounds.x + bounds.width || graphX + CELL_WIDTH < 0 || graphY > bounds.y + bounds.height
					|| graphY + CELL_HEIGHT < 0) {
				// Out of scope
			} else {
				Color color = cell.getColor();
				// g.drawImage(img, graphX, graphY, null); // TODO Use observer?
				g.setColor(color);
				g.fillRect(graphX, graphY, CELL_WIDTH, CELL_HEIGHT);
				g.setColor(Color.BLACK);
				g.drawRect(graphX, graphY, CELL_WIDTH, CELL_HEIGHT);
			}
		}
	}

	@Override
	public Iterator<ViewCell> iterator() {
		long minWorldX = worldOrigin.getX();
		long maxWorldX = worldOrigin.getX() + width - 1;
		long minWorldY = worldOrigin.getY();
		long maxWorldY = worldOrigin.getY() + height - 1;

		return new Iterator<ViewCell>() {
			long worldX = minWorldX - 1;// -1 so the next one is the first one
			long worldY = minWorldY;

			@Override
			public boolean hasNext() {
				return worldX < maxWorldX || worldY < maxWorldY;
			}

			private void passToNext() {
				if (worldX < maxWorldX) {
					worldX++;
				} else if (worldY < maxWorldY) {
					worldX = minWorldX;
					worldY++;
				} else {
					throw new NoSuchElementException("Last cell reached");

				}
			}

			@Override
			public ViewCell next() {
				passToNext();
				WorldCell worldCell = world.getCell(new WorldPosition(worldX, worldY));
				int viewX = (int) (worldX - minWorldX) * 32;
				int viewY = (int) (worldY - minWorldY) * 32;
				ViewPosition position = new ViewPosition(viewX, viewY);
				return new ViewCell(worldCell, position);
			}
		};
	}

	class ViewCell {

		private final WorldCell cell;
		private final ViewPosition position;

		public ViewCell(WorldCell cell, ViewPosition position) {
			this.cell = cell;
			this.position = position;
		}

		public Color getColor() {
			switch (cell.getType()) {
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
				throw new RuntimeException("Unrendered type: " + cell.getType());
			}
		}

		public ViewPosition getOrigin() {
			return new ViewPosition(0, 0);
		}

		public ViewPosition getPosition() {
			return position;
		}
	}
}
