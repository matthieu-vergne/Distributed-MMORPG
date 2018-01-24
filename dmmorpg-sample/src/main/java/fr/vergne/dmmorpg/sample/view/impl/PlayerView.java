package fr.vergne.dmmorpg.sample.view.impl;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.vergne.dmmorpg.View;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldCell;

public class PlayerView implements View<World, WorldCell, Graphics, Rectangle> {

	private final Player player;
	private final Scaler scaler;

	public PlayerView(Player player, Scaler scaler) {
		this.player = player;
		this.scaler = scaler;
	}

	@Override
	public Iterator<Link<WorldCell, Rectangle>> map(World world, Graphics graphics) {
		return new CenteredView(world.getPosition(player), scaler).map(world, graphics);
	}

	@Override
	public String toString() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("player", player);
		data.put("scaler", scaler);
		return "View" + data;
	}
}
