package fr.vergne.dmmorpg.sample.view.impl.renderer;

import java.awt.Graphics;
import java.util.Collection;

import fr.vergne.dmmorpg.Renderer;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.world.WorldCell;

public class CellRenderer implements Renderer<WorldCell, Graphics> {

	private final Renderer<Object, Graphics> renderer;

	public CellRenderer(Renderer<Object, Graphics> renderer) {
		this.renderer = renderer;
	}

	@Override
	public void render(WorldCell cell, Graphics g) {
		renderer.render(cell.getZoneDescriptor(), g);

		Collection<Player> players = cell.getPlayers();
		for (Player player : players) {
			renderer.render(player, g);
		}
	}
}
