package fr.vergne.dmmorpg.sample.world;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;

import fr.vergne.dmmorpg.sample.world.WorldMap.PositionUpdate;

public class WorldMapTest {

	@Test
	public void testGetEmptyCollection() {
		WorldMap<String> map = new WorldMap<>();
		Collection<String> objects = map.getAllAt(new WorldPosition(62135, -18635));
		assertNotNull(objects);
		assertTrue(objects.isEmpty());
	}

	@Test
	public void testGetAllObjectsAtPosition() {
		WorldMap<String> map = new WorldMap<>();
		map.put("x", new WorldPosition(0, 0));
		map.put("y", new WorldPosition(0, 0));
		map.put("z", new WorldPosition(0, 0));
		Collection<String> objects = map.getAllAt(new WorldPosition(0, 0));
		assertEquals(3, objects.size());
		assertTrue(objects.contains("x"));
		assertTrue(objects.contains("y"));
		assertTrue(objects.contains("z"));
	}

	@Test
	public void testGetOnlyObjectsAtPosition() {
		WorldMap<String> map = new WorldMap<>();
		map.put("x", new WorldPosition(0, 0));
		map.put("y", new WorldPosition(10, 10));
		assertTrue(map.getAllAt(new WorldPosition(0, 0)).contains("x"));
		assertFalse(map.getAllAt(new WorldPosition(0, 0)).contains("y"));
		assertFalse(map.getAllAt(new WorldPosition(10, 10)).contains("x"));
		assertTrue(map.getAllAt(new WorldPosition(10, 10)).contains("y"));
	}

	@Test
	public void testPutExistingObjectAtNewPositionRemovesFromOldPosition() {
		WorldMap<String> map = new WorldMap<>();
		map.put("x", new WorldPosition(0, 0));
		map.put("x", new WorldPosition(10, 10));
		assertEquals(new WorldPosition(10, 10), map.getPosition("x"));
		assertFalse(map.getAllAt(new WorldPosition(0, 0)).contains("x"));
		assertTrue(map.getAllAt(new WorldPosition(10, 10)).contains("x"));
	}

	@Test
	public void testPutNotifiesUpdateOnNewObject() {
		WorldMap<String> map = new WorldMap<>();
		Collection<PositionUpdate<String>> updates = new LinkedList<>();
		map.listenUpdate(u -> updates.add(u));

		map.put("x", new WorldPosition(10, 10));
		assertFalse(updates.isEmpty());
	}

	@Test
	public void testObjectNotificationHasCorrectObject() {
		WorldMap<String> map = new WorldMap<>();
		Collection<PositionUpdate<String>> updates = new LinkedList<>();
		map.listenUpdate(u -> updates.add(u));

		map.put("x", new WorldPosition(10, 10));
		PositionUpdate<String> update = updates.iterator().next();
		assertEquals("x", update.getSource());
	}

	@Test
	public void testObjectNotificationHasCorrectNewPosition() {
		WorldMap<String> map = new WorldMap<>();
		Collection<PositionUpdate<String>> updates = new LinkedList<>();
		map.listenUpdate(u -> updates.add(u));

		map.put("x", new WorldPosition(10, 10));
		PositionUpdate<String> update = updates.iterator().next();
		assertEquals(new WorldPosition(10, 10), update.getNewValue());
	}

	@Test
	public void testObjectNotificationHasCorrectOldPositionForKnownObject() {
		WorldMap<String> map = new WorldMap<>();
		map.put("x", new WorldPosition(0, 0));
		Collection<PositionUpdate<String>> updates = new LinkedList<>();
		map.listenUpdate(u -> updates.add(u));

		map.put("x", new WorldPosition(10, 10));
		PositionUpdate<String> update = updates.iterator().next();
		assertEquals(new WorldPosition(0, 0), update.getOldValue());
	}

	@Test
	public void testObjectNotificationHasNullOldPositionForNewObject() {
		WorldMap<String> map = new WorldMap<>();
		Collection<PositionUpdate<String>> updates = new LinkedList<>();
		map.listenUpdate(u -> updates.add(u));

		map.put("x", new WorldPosition(10, 10));
		PositionUpdate<String> update = updates.iterator().next();
		assertNull(update.getOldValue());
	}

	@Test
	public void testPutAtSamePositionDoesNotNotify() {
		WorldMap<String> map = new WorldMap<>();
		map.put("x", new WorldPosition(10, 10));
		Collection<PositionUpdate<String>> updates = new LinkedList<>();
		map.listenUpdate(u -> updates.add(u));

		map.put("x", new WorldPosition(10, 10));
		assertTrue(updates.isEmpty());
	}
}
