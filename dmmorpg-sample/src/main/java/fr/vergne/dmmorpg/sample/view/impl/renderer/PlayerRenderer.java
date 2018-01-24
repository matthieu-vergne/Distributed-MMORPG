package fr.vergne.dmmorpg.sample.view.impl.renderer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.vergne.dmmorpg.Renderer;
import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.player.Player;

public class PlayerRenderer implements Renderer<Player, Graphics> {

	@Override
	public void render(Player player, Graphics g) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File("res/avatar.png"));
		} catch (IOException cause) {
			cause.printStackTrace();
			return;
		}

		int imageIndex = getImageIndex(player.getDirection());
		Rectangle bounds = g.getClipBounds();
		int scale = bounds.width / image.getWidth();
		int heightOffset = imageIndex * (bounds.height / scale);
		g.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, 0, heightOffset, image.getWidth(),
				heightOffset + (bounds.height / scale), null);
	}

	private int getImageIndex(Direction direction) {
		switch (direction) {
		case BOTTOM:
			return 0;
		case LEFT:
			return 1;
		case RIGHT:
			return 2;
		case TOP:
			return 3;
		default:
			throw new IllegalStateException("Unmanaged direction: " + direction);
		}
	}
}
