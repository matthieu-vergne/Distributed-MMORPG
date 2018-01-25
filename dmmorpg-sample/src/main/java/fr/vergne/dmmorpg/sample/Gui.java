package fr.vergne.dmmorpg.sample;

import static fr.vergne.dmmorpg.sample.AWTKeyConsumer.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.view.impl.PlayerView;
import fr.vergne.dmmorpg.sample.view.impl.Scaler;
import fr.vergne.dmmorpg.sample.view.impl.renderer.CellRenderer;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldPosition;

@SuppressWarnings("serial")
public class Gui extends JFrame {

	public Gui() {
		World world = new World();
		Scaler scaler = new Scaler(32, 32);
		Player player = new Player('A', Color.RED);
		world.add(player, new WorldPosition(0, 0));
		PlayerView worldView = new PlayerView(player, scaler);

		setTitle("DMMORPG");
		setMinimumSize(new Dimension(64, 64));
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(1, 1));
		ViewComponent worldViewComponent = new ViewComponent(world, worldView, new CellRenderer());
		scaler.listenUpdate(u -> worldViewComponent.fireRepaint());
		getContentPane().add(worldViewComponent);

		configureKeyboard(world, player, scaler);
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
