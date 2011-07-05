package com.dmmorpg.util;

import java.awt.AWTException;
import java.awt.Robot;

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

	public static void setPositionOnScreen(int x, int y) {
		getRobot().mouseMove(x, y);
	}
}
