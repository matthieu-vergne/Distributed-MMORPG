package fr.vergne.dmmorpg.sample;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import fr.vergne.dmmorpg.sample.view.impl.PositionedView;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldPosition;

@SuppressWarnings("serial")
public class Gui extends JFrame {

	private final ViewComponent worldViewComponent;

	public Gui() {
		setTitle("DMMORPG");
		setPreferredSize(new Dimension(500, 400));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(1, 1));
		worldViewComponent = new ViewComponent();
		getContentPane().add(worldViewComponent);

		World world = new World();
		worldViewComponent.setView(new PositionedView(world, new WorldPosition(0, 0), 10));

		pack();
	}

	public static void main(String[] args) {
		new Thread(() -> new Gui().setVisible(true)).start();
	}
}
