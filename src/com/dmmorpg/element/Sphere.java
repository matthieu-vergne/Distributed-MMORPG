package com.dmmorpg.element;

import javax.media.j3d.Node;

public class Sphere extends Default3DElement {
	@Override
	protected Node initNode() {
		return new com.sun.j3d.utils.geometry.Sphere(0.25f);
	}
}
