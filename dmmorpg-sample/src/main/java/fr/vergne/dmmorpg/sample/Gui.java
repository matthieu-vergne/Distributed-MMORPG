package fr.vergne.dmmorpg.sample;

import static fr.vergne.dmmorpg.sample.AWTKeyConsumer.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFrame;

import fr.vergne.dmmorpg.Renderer;
import fr.vergne.dmmorpg.impl.RendererComposer;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.view.impl.PlayerView;
import fr.vergne.dmmorpg.sample.view.impl.Scaler;
import fr.vergne.dmmorpg.sample.view.impl.renderer.Filler;
import fr.vergne.dmmorpg.sample.view.impl.renderer.PlayerRenderer;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldCell;
import fr.vergne.dmmorpg.sample.world.WorldPosition;
import fr.vergne.dmmorpg.sample.zone.AccessPolicy;
import fr.vergne.dmmorpg.sample.zone.Zone;
import fr.vergne.dmmorpg.sample.zone.impl.StaticZoneDescriptor;
import fr.vergne.dmmorpg.sample.zone.impl.ZoneBuilder;

@SuppressWarnings("serial")
public class Gui extends JFrame {

	public Gui() {
		World world = new World();
		Zone.Descriptor water = new StaticZoneDescriptor(AccessPolicy.BLOCK_ALL);
		Zone.Descriptor earth = new StaticZoneDescriptor(AccessPolicy.ALLOW_ALL);
		Zone.Descriptor snow = new StaticZoneDescriptor(AccessPolicy.ALLOW_ALL);
		{
			ZoneBuilder builder = new ZoneBuilder();
			builder.setDefault(water);
			builder.add(earth, p -> -10 < p.getX() && p.getX() < 10 && -10 < p.getY() && p.getY() < 10);
			builder.add(snow, p -> -3 < p.getX() && p.getX() < 3 && -2 < p.getY() && p.getY() < 2);
			world.setGround(builder.build());
		}
		Player player = new Player();
		world.add(player, new WorldPosition(0, 0));

		Scaler scaler = new Scaler(32, 32);
		PlayerView worldView = new PlayerView(player, scaler);
		configureKeyboard(world, player, scaler);

		Renderer<WorldCell, Graphics> cellRenderer;
		{
			RendererComposer<Graphics> builder = new RendererComposer<>();
			builder.set(water, new Filler<>(Color.BLUE));
			builder.set(earth, new Filler<>(Color.ORANGE));
			builder.set(snow, new Filler<>(Color.WHITE));
			builder.set(player, new PlayerRenderer(new File("res/avatar.png")));
			Renderer<Object, Graphics> renderer = builder.build();
			cellRenderer = (cell, graphics) -> {
				renderer.render(cell.getZoneDescriptor(), graphics);
				cell.getPlayers().forEach(p -> renderer.render(p, graphics));
			};
		}
		ViewComponent worldViewComponent = new ViewComponent(world, worldView, cellRenderer);
		scaler.listenUpdate(u -> worldViewComponent.fireRepaint());

		setTitle("DMMORPG");
		setMinimumSize(new Dimension(64, 64));
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(1, 1));
		getContentPane().add(worldViewComponent);
		pack();
	}

	private void configureKeyboard(World world, Player player, Scaler scaler) {
		// TODO Similarly make a MouseConsumer
		KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		AWTKeyConsumer consumer = new AWTKeyConsumer();
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_LEFT), event -> world.actTowards(player, Direction.LEFT));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_RIGHT), event -> world.actTowards(player, Direction.RIGHT));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_UP), event -> world.actTowards(player, Direction.TOP));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_DOWN), event -> world.actTowards(player, Direction.BOTTOM));
		consumer.addConsumer(whenCharTyped('+'), event -> scaler.increaseScale());
		consumer.addConsumer(whenCharTyped('-'), event -> scaler.decreaseScale());
		focusManager.addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent event) {
				return consumer.consume(event);
			}
		});
	}

	public static void main(String[] args) {
		new Thread(() -> new Gui().setVisible(true)).start();
	}
}
