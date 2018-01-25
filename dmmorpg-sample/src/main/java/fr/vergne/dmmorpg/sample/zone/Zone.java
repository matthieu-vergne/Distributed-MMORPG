package fr.vergne.dmmorpg.sample.zone;

import fr.vergne.dmmorpg.sample.world.AccessPolicy;
import fr.vergne.dmmorpg.sample.world.WorldPosition;

public interface Zone {
	public Descriptor getDescriptor(WorldPosition position);

	public interface Descriptor {

		public <T> AccessPolicy<? super T> getAccessPolicy(T t);
	}
}
