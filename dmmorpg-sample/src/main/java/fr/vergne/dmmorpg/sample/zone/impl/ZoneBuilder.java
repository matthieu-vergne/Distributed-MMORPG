package fr.vergne.dmmorpg.sample.zone.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.vergne.dmmorpg.sample.world.WorldPosition;
import fr.vergne.dmmorpg.sample.zone.Zone;

public class ZoneBuilder {

	private Zone.Type defaultType;
	private final List<Delimiter> layers = new LinkedList<>();
	private final Map<Delimiter, Zone.Type> types = new HashMap<>();

	public void setDefault(Zone.Type type) {
		this.defaultType = type;
	}

	public void add(Zone.Type type, Delimiter delimiter) {
		layers.add(0, delimiter);
		types.put(delimiter, type);
	}

	public static interface Delimiter {
		public boolean isAt(WorldPosition position);
	}

	public Zone build() {
		Zone.Type defaultType = this.defaultType;
		List<Delimiter> layers = new ArrayList<>(this.layers);
		Map<Delimiter, Zone.Type> types = new HashMap<>(this.types);
		return new Zone() {

			@Override
			public Type getType(WorldPosition position) {
				for (Delimiter delimiter : layers) {
					if (delimiter.isAt(position)) {
						return types.get(delimiter);
					} else {
						continue;
					}
				}
				return defaultType;
			}
		};
	}
}
