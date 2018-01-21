package fr.vergne.dmmorpg.sample.view.impl;

import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.view.View;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;

public class PlayerView implements View<Graphics> {

	private final World world;
	private final Player player;
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();
	private final Listener<? super WorldUpdate> updateListener = update -> Updatable.fireUpdate(listeners, update);
	private final Scaler scaler = new Scaler();

	public PlayerView(World world, Player player) {
		this.world = world;
		this.world.listenUpdate(updateListener);
		this.player = player;
		this.scaler.listenUpdate(updateListener);
	}

	@Override
	protected void finalize() throws Throwable {
		world.unlistenUpdate(updateListener);
		super.finalize();
	}

	public void increaseScale() {
		scaler.increaseScale();
	}

	public void decreaseScale() {
		scaler.decreaseScale();
	}

	@Override
	public void render(Graphics g) {
		new Renderer().render(g, scaler.getScale(), world, world.getPosition(player));
	}

	@Override
	public void listenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.add(listener);
	}

	@Override
	public void unlistenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.remove(listener);
	}

	@Override
	public String toString() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("world", world);
		data.put("player", player);
		return "View" + data;
	}
}
