package fr.vergne.dmmorpg.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class KeyConsumer<KeyEvent> {

	private final Map<EventFilter<KeyEvent>, EventConsumer<KeyEvent>> consumers = new HashMap<>();

	public void addConsumer(EventFilter<KeyEvent> selector, EventConsumer<KeyEvent> consumer) {
		consumers.put(selector, consumer);
	}

	public boolean consume(KeyEvent event) {
		for (Entry<EventFilter<KeyEvent>, EventConsumer<KeyEvent>> entry : consumers.entrySet()) {
			if (entry.getKey().isRelevantEvent(event)) {
				entry.getValue().consume(event);
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	public interface EventFilter<KeyEvent> {
		boolean isRelevantEvent(KeyEvent event);
	}

	public interface EventConsumer<KeyEvent> {
		void consume(KeyEvent event);
	}
}
