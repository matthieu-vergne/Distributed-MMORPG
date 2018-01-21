package fr.vergne.dmmorpg.sample.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import fr.vergne.dmmorpg.Renderable;
import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.Direction;
import fr.vergne.dmmorpg.sample.Property;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;

public class Player implements Updatable<WorldUpdate>, Renderable<Graphics> {

	private final char character;
	private final Color color;
	private Direction direction = Direction.BOTTOM;
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();

	public Player(char character, Color color) {
		this.character = character;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public char getCharacter() {
		return character;
	}

	public void setDirection(Direction direction) {
		Direction old = direction;
		this.direction = direction;
		Updatable.fireUpdate(listeners, new WorldUpdate(this, Property.WORLD_DIRECTION, old, direction));
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return "" + character;
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
	public void render(Graphics g) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File("res/avatar.png"));
		} catch (IOException cause) {
			cause.printStackTrace();
			return;
		}

		int imageIndex = getImageIndex();
		Rectangle bounds = g.getClipBounds();
		int scale = bounds.width / image.getWidth();
		int heightOffset = imageIndex * (bounds.height / scale);
		g.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, 0, heightOffset, image.getWidth(),
				heightOffset + (bounds.height / scale), null);
	}

	private int getImageIndex() {
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
