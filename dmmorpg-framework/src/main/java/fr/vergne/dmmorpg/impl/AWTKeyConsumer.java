package fr.vergne.dmmorpg.impl;

import java.awt.event.KeyEvent;

import fr.vergne.dmmorpg.KeyConsumer;

public class AWTKeyConsumer extends KeyConsumer<KeyEvent> {

	public static EventFilter<KeyEvent> whenAnyKeyPressed() {
		return event -> event.getID() == KeyEvent.KEY_PRESSED;
	}

	public static EventFilter<KeyEvent> whenKeyPressed(int key) {
		return event -> event.getID() == KeyEvent.KEY_PRESSED && event.getKeyCode() == key;
	}

	public static EventFilter<KeyEvent> whenAnyKeyReleased() {
		return event -> event.getID() == KeyEvent.KEY_RELEASED;
	}

	public static EventFilter<KeyEvent> whenKeyReleased(int key) {
		return event -> event.getID() == KeyEvent.KEY_RELEASED && event.getKeyCode() == key;
	}

	public static EventFilter<KeyEvent> whenAnyCharTyped() {
		return event -> event.getID() == KeyEvent.KEY_TYPED && event.getKeyChar() != KeyEvent.CHAR_UNDEFINED;
	}

	public static EventFilter<KeyEvent> whenCharTyped(char c) {
		return event -> event.getID() == KeyEvent.KEY_TYPED && event.getKeyChar() == c;
	}

}
