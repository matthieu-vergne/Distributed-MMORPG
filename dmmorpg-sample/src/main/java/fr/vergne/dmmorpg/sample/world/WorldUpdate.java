package fr.vergne.dmmorpg.sample.world;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.vergne.dmmorpg.impl.FinalUpdate;
import fr.vergne.dmmorpg.sample.Property;

public class WorldUpdate extends FinalUpdate<Object, Property, Object> {

	public WorldUpdate(Object source, Property property, Object oldValue, Object newValue) {
		super(source, property, oldValue, newValue);
	}

	@Override
	public String toString() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("source", getSource());
		data.put("property", getProperty());
		data.put("old", getOldValue());
		data.put("new", getNewValue());
		return data.toString();
	}
}
