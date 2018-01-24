package fr.vergne.dmmorpg.sample.view.impl.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collection;

import fr.vergne.dmmorpg.Renderer;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.world.WorldCell;

public class CellRenderer implements Renderer<WorldCell, Graphics> {

	private final Renderer<Player, Graphics> playerRenderer = new PlayerRenderer();

	@Override
	public void render(WorldCell worldCell, Graphics g) {
		Rectangle cellBounds = g.getClipBounds();
		Color color = getGroundColor(worldCell);
		g.setColor(color);
		g.fillRect(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);
		g.setColor(color.darker());
		g.drawRect(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);

		Collection<Player> players = worldCell.getPlayers();
		for (Player player : players) {
			playerRenderer.render(player, g);
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
