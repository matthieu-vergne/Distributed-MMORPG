package com.dmmorpg.element;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

//TODO create tests
//TODO write javadocs
public class Reactor extends Default3DElement {
	@Override
	protected Node initNode() {
		return new Shape3D(mkGeometry0(0.5f, 0.85f, 0.8f, 25, new Color3f(1f,
				0f, 1f)), createApp("LINE"));
	}

	private Geometry mkGeometry0(float radius1, float radius2, float height,
			int nbPane, Color3f color) {
		int tab[] = new int[1];
		tab[0] = (nbPane * 2) + 2;
		TriangleStripArray geom0 = new TriangleStripArray((nbPane * 2) + 2,
				TriangleStripArray.COORDINATES | TriangleStripArray.COLOR_3,
				tab);

		for (int i = 0; i < (nbPane * 2) + 2; i++)
			geom0.setColor(i, color);

		double angle = 2 * Math.PI / nbPane;

		Point3f point = new Point3f();
		for (int i = 0; i < nbPane; i++) {
			if (i == 0) {
				point.x = (float) (radius1 * Math.cos(0 * angle));
				point.y = (float) height;
				point.z = (float) (radius1 * Math.sin(0 * angle));
				geom0.setCoordinate(0, point);

				point.x = (float) (radius2 * Math.cos(0 * angle));
				point.y = 0f;
				point.z = (float) (radius2 * Math.sin(0 * angle));
				geom0.setCoordinate(1, point);

				point.x = (float) (radius1 * Math.cos(1 * angle));
				point.y = (float) height;
				point.z = (float) (radius1 * Math.sin(1 * angle));
				geom0.setCoordinate(2, point);

				point.x = (float) (radius2 * Math.cos(1 * angle));
				point.y = 0f;
				point.z = (float) (radius2 * Math.sin(1 * angle));
				geom0.setCoordinate(3, point);
			} else {
				point.x = (float) (radius1 * Math.cos((i + 1) * angle));
				point.y = (float) height;
				point.z = (float) (radius1 * Math.sin((i + 1) * angle));
				geom0.setCoordinate((i + 1) * 2, point);

				point.x = (float) (radius2 * Math.cos((i + 1) * angle));
				point.y = 0f;
				point.z = (float) (radius2 * Math.sin((i + 1) * angle));
				geom0.setCoordinate((i + 1) * 2 + 1, point);
			}
		}

		return geom0;
	}

	private Appearance createApp(String str) {
		Appearance app = new Appearance();
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		if (str.equals("LINE"))
			polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		app.setPolygonAttributes(polyAttrib);

		return app;
	}
}
