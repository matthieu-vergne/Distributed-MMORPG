package com.dmmorpg.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import org.junit.Test;

public class MouseUtilTest {

	private class EventWaiter<E> {
		public final List<E> events = Collections
				.synchronizedList(new ArrayList<E>());
		public boolean ready = false;
	}

	@Test
	public void testSetPositionOnScreen() {
		final EventWaiter<MouseEvent> waiter = new EventWaiter<MouseEvent>();
		new Thread() {
			@Override
			public void run() {
				JFrame frame = new JFrame();
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setBounds(0, 0, screen.width, screen.height);
				frame.setVisible(true);
				frame.addMouseMotionListener(new MouseMotionListener() {

					@Override
					public void mouseMoved(MouseEvent e) {
						waiter.events.add(e);
						waiter.ready = true;
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						// do nothing
					}
				});
				try {
					sleep(1000);
				} catch (InterruptedException e1) {
					fail("sleeping interrupted : " + e1.getLocalizedMessage());
				}
				waiter.ready = true;
			}
		}.run();

		waitting(waiter);

		{
			MouseUtil.setPositionOnScreen(50, 50);
			waitting(waiter);
			assertEquals(1, waiter.events.size());
			Point location = waiter.events.get(0).getLocationOnScreen();
			assertEquals(50, location.x);
			assertEquals(50, location.y);
			waiter.events.remove(0);
		}

		{
			MouseUtil.setPositionOnScreen(100, 100);
			waitting(waiter);
			assertEquals(1, waiter.events.size());
			Point location = waiter.events.get(0).getLocationOnScreen();
			assertEquals(100, location.x);
			assertEquals(100, location.y);
			waiter.events.remove(0);
		}
	}

	private static void waitting(EventWaiter<?> waiter) {
		long start = System.currentTimeMillis();
		while (!waiter.ready) {
			if (System.currentTimeMillis() - start > 10000) {
				fail("timeout");
			}
		}
		waiter.ready = false;
	}
}
