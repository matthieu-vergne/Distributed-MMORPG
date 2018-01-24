package fr.vergne.dmmorpg.sample.view.impl;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;

import fr.vergne.dmmorpg.View;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldCell;

public class ProxyView implements View<World, WorldCell, Graphics, Rectangle> {

	private View<World, WorldCell, Graphics, Rectangle> subview;

	public void setSubview(View<World, WorldCell, Graphics, Rectangle> subview) {
		this.subview = subview;
	}

	public View<World, WorldCell, Graphics, Rectangle> getSubview() {
		return subview;
	}

	@Override
	public Iterator<Link<WorldCell, Rectangle>> map(World world, Graphics graphics) {
		return subview.map(world, graphics);
	}

}
