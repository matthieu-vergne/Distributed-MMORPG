package com.dmmorpg.element;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class Sphere extends DefaultElement {
	private final com.sun.j3d.utils.geometry.Sphere sphere;
	private final TransformGroup sphereTransGroup = new TransformGroup();
	private final Transform3D SphereTrans = new Transform3D();
	private BranchGroup branchGroup;
	private float sphereX = 0;
	private float sphereY = 0;

	public Sphere(float radius) {
		sphere = new com.sun.j3d.utils.geometry.Sphere(radius);
		System.out.println(sphere
				.getAppearance()
				.getTransparencyAttributes());
		Transform3D pos1 = new Transform3D();
		pos1.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
		SphereTrans.setScale(new Vector3d(1.0, .8, 1.0));
		sphereTransGroup.setTransform(pos1);
		sphereTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereTransGroup.addChild(sphere);
	}

	public void putInBranchGroup(BranchGroup branchGroup) {
		if (this.branchGroup != null) {
			this.branchGroup.removeChild(sphereTransGroup);
		}
		this.branchGroup = branchGroup;
		if (this.branchGroup != null) {
			this.branchGroup.addChild(sphereTransGroup);
		}
	}

	protected boolean hasChanged() {
		return true;
	}

	protected void redraw() {
		double time = (double) System.currentTimeMillis() / 1000;
		sphereX = (float) Math.cos(time) / 2;
		sphereY = (float) Math.sin(time) / 2;
		Vector3f coord = new Vector3f(sphereX, sphereY, 0.0f);
		SphereTrans.setTranslation(coord);
		sphereTransGroup.setTransform(SphereTrans);
	}
}
