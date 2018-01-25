package fr.vergne.dmmorpg.sample;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import fr.vergne.dmmorpg.Renderer;
import fr.vergne.dmmorpg.Updatable.Listener;
import fr.vergne.dmmorpg.View;
import fr.vergne.dmmorpg.View.Link;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldCell;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;

@SuppressWarnings("serial")
public class ViewComponent extends Component {

	private static final Color NO_DATA = new Color(0, 0, 0, 0);
	private final World world;
	private final View<World, WorldCell, Graphics, Rectangle> view;
	private final Renderer<WorldCell, Graphics> cellRenderer;
	private boolean isRepaintFired = false;
	private final Listener<WorldUpdate> viewListener = update -> fireRepaint();

	public ViewComponent(World world, View<World, WorldCell, Graphics, Rectangle> view,
			Renderer<WorldCell, Graphics> cellRenderer) {
		this.view = view;
		this.world = world;
		this.world.listenUpdate(viewListener);
		this.cellRenderer = cellRenderer;
	}

	@Override
	public void paint(Graphics g) {
		// TODO Check to paint efficiently:
		// http://www.oracle.com/technetwork/java/painting-140037.html
		Rectangle totalBounds = g.getClipBounds();

		g.setColor(NO_DATA);
		g.fillRect(totalBounds.x, totalBounds.y, totalBounds.width, totalBounds.height);

		Iterator<Link<WorldCell, Rectangle>> iterator = view.map(world, g);
		while (iterator.hasNext()) {
			Link<WorldCell, Rectangle> link = iterator.next();

			Rectangle bounds = link.getGraphicsCell();
			Graphics graphics = g.create(bounds.x, bounds.y, bounds.width, bounds.height);

			WorldCell cell = link.getWorldCell();
			cellRenderer.render(cell, graphics);
		}
	}

	public void fireRepaint() {
		if (!isRepaintFired) {
			isRepaintFired = true;
			SwingUtilities.invokeLater(() -> {
				repaint();
				isRepaintFired = false;
			});
		} else {
			// Repaint already requested
		}
	}
}
