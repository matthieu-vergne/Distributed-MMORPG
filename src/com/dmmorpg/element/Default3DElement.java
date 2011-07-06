package com.dmmorpg.element;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

//TODO create tests
//TODO write javadocs
public abstract class Default3DElement implements I3DElement {
	private final TransformGroup transformationGroup = new TransformGroup();
	private BranchGroup branchGroup;

	public Default3DElement() {
		Node node = initNode();
		if (node == null) {
			throw new NullPointerException(
					"the initialisation of the shape cannot be null");
		}
		transformationGroup.addChild(node);
		transformationGroup.setTransform(new Transform3D());
	}

	abstract protected Node initNode();

	public Node getNode() {
		return transformationGroup.getChild(0);
	}

	public void setBranchGroup(BranchGroup branchGroup) {
		if (this.branchGroup != branchGroup) {
			if (this.branchGroup != null) {
				this.branchGroup.removeChild(transformationGroup);
			}
			this.branchGroup = branchGroup;
			if (this.branchGroup != null) {
				this.branchGroup.addChild(transformationGroup);
			}
		}
	}

	public BranchGroup getBranchGroup() {
		return branchGroup;
	}

	public void setTransformation(Transform3D transformation) {
		if (transformation == null) {
			throw new NullPointerException(
					"the transformation cannot be null, maybe you want to remove the element from the branch group ?");
		}

		transformationGroup.setTransform(transformation);
	}

	public Transform3D getTransformation() {
		Transform3D transformation = new Transform3D();
		transformationGroup.getTransform(transformation);
		return transformation;
	}

	public void setPosition(double x, double y, double z) {
		Transform3D translationFromOrigin = getTransformation();
		translationFromOrigin.setTranslation(new Vector3d(x, y, z));
		setTransformation(translationFromOrigin);
	}
}
