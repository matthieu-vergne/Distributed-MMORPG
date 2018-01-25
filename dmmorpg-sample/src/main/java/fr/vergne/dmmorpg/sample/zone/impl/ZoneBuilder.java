package fr.vergne.dmmorpg.sample.zone.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.vergne.dmmorpg.sample.world.WorldPosition;
import fr.vergne.dmmorpg.sample.zone.Zone;
import fr.vergne.dmmorpg.sample.zone.Zone.Descriptor;

public class ZoneBuilder {

	private Descriptor defaultZone;
	private final List<Delimiter> layers = new LinkedList<>();
	private final Map<Delimiter, Descriptor> zones = new HashMap<>();

	public void setDefault(Descriptor zone) {
		this.defaultZone = zone;
	}

	public void add(Descriptor zone, Delimiter delimiter) {
		layers.add(0, delimiter);
		zones.put(delimiter, zone);
	}

	public static interface Delimiter {
		public boolean isAt(WorldPosition position);
	}

	public Zone build() {
		return new Zone() {

			@Override
			public Descriptor getDescriptor(WorldPosition position) {
				for (Delimiter delimiter : layers) {
					if (delimiter.isAt(position)) {
						return zones.get(delimiter);
					} else {
						continue;
					}
				}
				return defaultZone;
			}
		};
	}
}
