package com.dmmorpg.util;

import java.awt.AWTException;
import java.awt.Robot;

/**
 * This class offers several methods to manage the mouse.
 * 
 * @author Matthieu VERGNE <matthieu.vergne@gmail.com>
 * 
 */
public abstract class MouseUtil {
	private static Robot ROBOT;

	private static Robot getRobot() {
		if (ROBOT == null) {
			try {
				ROBOT = new Robot();
			} catch (AWTException ex) {
				throw new RuntimeException(ex);
			}
		}

		return ROBOT;
	}

	/**
	 * Place the mouse on the screen. As it is a normal movement, it generates a
	 * mouse event.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public static void setPositionOnScreen(int x, int y) {
		getRobot().mouseMove(x, y);
	}
}
