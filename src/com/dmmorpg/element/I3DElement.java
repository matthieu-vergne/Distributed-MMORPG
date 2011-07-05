package com.dmmorpg.element;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;

public interface I3DElement {

	public Node getNode();

	public void setTransformation(Transform3D transformation);

	public Transform3D getTransformation();

	public void setBranchGroup(BranchGroup branchGroup);

	public BranchGroup getBranchGroup();

	public void setPosition(double x, double y, double z);
}
