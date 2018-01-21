package fr.vergne.dmmorpg.sample;

import static fr.vergne.dmmorpg.impl.AWTKeyConsumer.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import fr.vergne.dmmorpg.impl.AWTKeyConsumer;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.view.impl.PlayerView;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldPosition;

@SuppressWarnings("serial")
public class Gui extends JFrame {

	private final PlayerView worldView;

	public Gui() {
		setTitle("DMMORPG");
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(1, 1));
		ViewComponent worldViewComponent = new ViewComponent();
		getContentPane().add(worldViewComponent);

		World world = new World();
		Player player = new Player('A', Color.RED);
		world.add(player, new WorldPosition(0, 0));
		worldView = new PlayerView(world, player);
		worldViewComponent.setView(worldView);

		configureKeyboard(world, player);
		pack();
	}

	private void configureKeyboard(World world, Player player) {
		// TODO Similarly make a MouseConsumer
		KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		AWTKeyConsumer consumer = new AWTKeyConsumer();
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_LEFT), event -> world.actTowards(player, Direction.LEFT));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_RIGHT), event -> world.actTowards(player, Direction.RIGHT));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_UP), event -> world.actTowards(player, Direction.TOP));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_DOWN), event -> world.actTowards(player, Direction.BOTTOM));
		consumer.addConsumer(whenCharTyped('+'), event -> worldView.increaseScale());
		consumer.addConsumer(whenCharTyped('-'), event -> worldView.decreaseScale());
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
