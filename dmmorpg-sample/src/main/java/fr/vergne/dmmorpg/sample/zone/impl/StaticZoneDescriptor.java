package fr.vergne.dmmorpg.sample.zone.impl;

import fr.vergne.dmmorpg.sample.zone.AccessPolicy;
import fr.vergne.dmmorpg.sample.zone.Zone.Descriptor;

public class StaticZoneDescriptor implements Descriptor {

	private final AccessPolicy<Object> accessPolicy;

	public StaticZoneDescriptor(AccessPolicy<Object> accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	@Override
	public <T> AccessPolicy<? super T> getAccessPolicy(T t) {
		return accessPolicy;
	}

}
