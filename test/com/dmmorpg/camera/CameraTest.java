package com.dmmorpg.camera;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.junit.Test;

public class CameraTest {
	private static final double EPSILON = Double.parseDouble("1E-14");

	private static final void assertTupleEquals(Tuple3d expected, Tuple3d actual) {
		try {
			assertEquals(expected.x, actual.x, EPSILON);
			assertEquals(expected.y, actual.y, EPSILON);
			assertEquals(expected.z, actual.z, EPSILON);
		} catch (AssertionError ex) {
			fail("expected " + expected + " but was " + actual);
		}
	}

	public static void assertNormalized(Vector3d vector) {
		assertEquals(1, vector.length(), EPSILON);
	}

	private static void assertOrthogonal(Vector3d... vectors) {
		for (Vector3d v1 : vectors) {
			for (Vector3d v2 : vectors) {
				if (v1 != v2) {
					assertEquals(0, v1.dot(v2), EPSILON);
				}
			}
		}
	}

	@Test
	public void testKeepNormalizationOfAxis() {
		Camera camera = new Camera();

		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.goFront(3.2);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.goBack(5);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.goLeft(6);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.goRight(2.5);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.goUp(112);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.goDown(12.3);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.lookUp(3.5);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.lookDown(53);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.lookLeft(64);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.lookRight(35.2);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.inclineLeft(12.5);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());

		camera.inclineRight(95);
		assertNormalized(camera.getFrontVector());
		assertNormalized(camera.getLeftVector());
		assertNormalized(camera.getUpVector());
	}

	@Test
	public void testKeepOrthogonalityOfAxis() {
		Camera camera = new Camera();

		{
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.goFront(3.2);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.goBack(5);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.goLeft(6);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.goRight(2.5);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.goUp(112);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.goDown(12.3);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.lookUp(3.5);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.lookDown(53);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.lookLeft(64);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.lookRight(35.2);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.inclineLeft(12.5);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}

		{
			camera.inclineRight(95);
			assertOrthogonal(camera.getFrontVector(), camera.getUpVector(),
					camera.getLeftVector());
		}
	}

	@Test
	public void testGoToDirection() {
		Camera camera = new Camera();

		{
			camera.setPosition(new Point3d(0, 0, 0));
			camera.goTo(new Vector3d(-3, 5, 4), 0);
			assertTupleEquals(new Point3d(0, 0, 0), camera.getPosition());
		}

		{
			camera.setPosition(new Point3d(0, 0, 0));
			camera.goTo(new Vector3d(0, 0, 0), 1000);
			assertTupleEquals(new Point3d(0, 0, 0), camera.getPosition());
		}

		{
			camera.setPosition(new Point3d(0, 0, 0));
			Vector3d delta = new Vector3d(-8.2, 2.5, 3);
			camera.goTo(delta, 1);
			assertTupleEquals(delta, camera.getPosition());
		}

		{
			camera.setPosition(new Point3d(0, 0, 0));
			camera.goTo(new Vector3d(7, 5, 9), 10);
			assertTupleEquals(new Point3d(70, 50, 90), camera.getPosition());
		}
	}

	@Test
	public void testGoFront() {
		Camera camera = new Camera();
		double distance = 4.1;
		Vector3d movement = new Vector3d(camera.getFrontVector());
		movement.scale(distance);
		camera.goFront(distance);
		assertTupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoBack() {
		Camera camera = new Camera();
		double distance = 3.2;
		Vector3d movement = new Vector3d(camera.getFrontVector());
		movement.scale(-distance);
		camera.goBack(distance);
		assertTupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoLeft() {
		Camera camera = new Camera();
		double distance = 4.6;
		Vector3d movement = new Vector3d(camera.getLeftVector());
		movement.scale(distance);
		camera.goLeft(distance);
		assertTupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoRight() {
		Camera camera = new Camera();
		double distance = 1.2;
		Vector3d movement = new Vector3d(camera.getLeftVector());
		movement.scale(-distance);
		camera.goRight(distance);
		assertTupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoUp() {
		Camera camera = new Camera();
		double distance = 65.2;
		Vector3d movement = new Vector3d(camera.getUpVector());
		movement.scale(distance);
		camera.goUp(distance);
		assertTupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoDown() {
		Camera camera = new Camera();
		double distance = 4;
		Vector3d movement = new Vector3d(camera.getUpVector());
		movement.scale(-distance);
		camera.goDown(distance);
		assertTupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testLookUp() {
		Camera camera = new Camera();
		Vector3d frontVector = new Vector3d(camera.getFrontVector());
		Vector3d leftVector = new Vector3d(camera.getLeftVector());
		Vector3d upVector = new Vector3d(camera.getUpVector());
		Vector3d temp = frontVector;
		frontVector = upVector;
		upVector = temp;
		upVector.negate();

		camera.lookUp(Math.PI / 2);
		assertTupleEquals(frontVector, camera.getFrontVector());
		assertTupleEquals(leftVector, camera.getLeftVector());
		assertTupleEquals(upVector, camera.getUpVector());
	}

	@Test
	public void testLookDown() {
		Camera camera = new Camera();
		Vector3d frontVector = new Vector3d(camera.getFrontVector());
		Vector3d leftVector = new Vector3d(camera.getLeftVector());
		Vector3d upVector = new Vector3d(camera.getUpVector());
		Vector3d temp = frontVector;
		frontVector = upVector;
		upVector = temp;
		frontVector.negate();

		camera.lookDown(Math.PI / 2);
		assertTupleEquals(frontVector, camera.getFrontVector());
		assertTupleEquals(leftVector, camera.getLeftVector());
		assertTupleEquals(upVector, camera.getUpVector());
	}

	@Test
	public void testLookLeft() {
		Camera camera = new Camera();
		Vector3d frontVector = new Vector3d(camera.getFrontVector());
		Vector3d leftVector = new Vector3d(camera.getLeftVector());
		Vector3d upVector = new Vector3d(camera.getUpVector());
		Vector3d temp = frontVector;
		frontVector = leftVector;
		leftVector = temp;
		leftVector.negate();

		camera.lookLeft(Math.PI / 2);
		assertTupleEquals(frontVector, camera.getFrontVector());
		assertTupleEquals(leftVector, camera.getLeftVector());
		assertTupleEquals(upVector, camera.getUpVector());
	}

	@Test
	public void testLookRight() {
		Camera camera = new Camera();
		Vector3d frontVector = new Vector3d(camera.getFrontVector());
		Vector3d leftVector = new Vector3d(camera.getLeftVector());
		Vector3d upVector = new Vector3d(camera.getUpVector());
		Vector3d temp = frontVector;
		frontVector = leftVector;
		leftVector = temp;
		frontVector.negate();

		camera.lookRight(Math.PI / 2);
		assertTupleEquals(frontVector, camera.getFrontVector());
		assertTupleEquals(leftVector, camera.getLeftVector());
		assertTupleEquals(upVector, camera.getUpVector());
	}

	@Test
	public void testInclineLeft() {
		Camera camera = new Camera();
		Vector3d frontVector = new Vector3d(camera.getFrontVector());
		Vector3d leftVector = new Vector3d(camera.getLeftVector());
		Vector3d upVector = new Vector3d(camera.getUpVector());
		Vector3d temp = upVector;
		upVector = leftVector;
		leftVector = temp;
		leftVector.negate();

		camera.inclineLeft(Math.PI / 2);
		assertTupleEquals(frontVector, camera.getFrontVector());
		assertTupleEquals(leftVector, camera.getLeftVector());
		assertTupleEquals(upVector, camera.getUpVector());
	}

	@Test
	public void testInclineRight() {
		Camera camera = new Camera();
		Vector3d frontVector = new Vector3d(camera.getFrontVector());
		Vector3d leftVector = new Vector3d(camera.getLeftVector());
		Vector3d upVector = new Vector3d(camera.getUpVector());
		Vector3d temp = upVector;
		upVector = leftVector;
		leftVector = temp;
		upVector.negate();

		camera.inclineRight(Math.PI / 2);
		assertTupleEquals(frontVector, camera.getFrontVector());
		assertTupleEquals(leftVector, camera.getLeftVector());
		assertTupleEquals(upVector, camera.getUpVector());
	}

	@Test
	public void testSetOrientation() {
		Camera camera = new Camera();

		{
			Vector3d frontVector = new Vector3d(1, 0, 0);
			Vector3d leftVector = new Vector3d(0, 1, 0);
			Vector3d upVector = new Vector3d(0, 0, 1);
			camera.setOrientation(frontVector, upVector);
			assertTupleEquals(frontVector, camera.getFrontVector());
			assertTupleEquals(upVector, camera.getUpVector());
			assertTupleEquals(leftVector, camera.getLeftVector());
		}

		{
			Vector3d frontVector = new Vector3d(2, 0, 0);
			Vector3d leftVector = new Vector3d(0, 3, 0);
			Vector3d upVector = new Vector3d(0, 0, 4);
			camera.setOrientation(frontVector, upVector);
			frontVector.normalize();
			leftVector.normalize();
			upVector.normalize();
			assertTupleEquals(frontVector, camera.getFrontVector());
			assertTupleEquals(upVector, camera.getUpVector());
			assertTupleEquals(leftVector, camera.getLeftVector());
		}
	}

	@Test
	public void testTurnOn() {
		Camera camera = new Camera();

		{
			Vector3d frontVector = new Vector3d(1, 0, 0);
			Vector3d leftVector = new Vector3d(0, 1, 0);
			Vector3d upVector = new Vector3d(0, 0, 1);
			camera.setOrientation(frontVector, upVector);
			camera.turnOn(new Vector3d(5, 6, 4), 0);
			assertTupleEquals(frontVector, camera.getFrontVector());
			assertTupleEquals(leftVector, camera.getLeftVector());
			assertTupleEquals(upVector, camera.getUpVector());
		}

		{
			Vector3d frontVector = new Vector3d(1, 0, 0);
			Vector3d leftVector = new Vector3d(0, 1, 0);
			Vector3d upVector = new Vector3d(0, 0, 1);
			camera.setOrientation(frontVector, upVector);
			Vector3d temp = leftVector;
			leftVector = upVector;
			upVector = temp;
			leftVector.negate();
			camera.turnOn(new Vector3d(1, 0, 0), Math.PI / 2);
			assertTupleEquals(frontVector, camera.getFrontVector());
			assertTupleEquals(leftVector, camera.getLeftVector());
			assertTupleEquals(upVector, camera.getUpVector());
		}

		{
			Vector3d frontVector = new Vector3d(1, 0, 0);
			Vector3d leftVector = new Vector3d(0, 1, 0);
			Vector3d upVector = new Vector3d(0, 0, 1);
			camera.setOrientation(frontVector, upVector);
			Vector3d temp = frontVector;
			frontVector = upVector;
			upVector = temp;
			upVector.negate();
			camera.turnOn(new Vector3d(0, 1, 0), Math.PI / 2);
			assertTupleEquals(frontVector, camera.getFrontVector());
			assertTupleEquals(leftVector, camera.getLeftVector());
			assertTupleEquals(upVector, camera.getUpVector());
		}

		{
			Vector3d frontVector = new Vector3d(1, 0, 0);
			Vector3d leftVector = new Vector3d(0, 1, 0);
			Vector3d upVector = new Vector3d(0, 0, 1);
			camera.setOrientation(frontVector, upVector);
			Vector3d temp = frontVector;
			frontVector = leftVector;
			leftVector = temp;
			frontVector.negate();
			camera.turnOn(new Vector3d(0, 0, 1), Math.PI / 2);
			assertTupleEquals(frontVector, camera.getFrontVector());
			assertTupleEquals(leftVector, camera.getLeftVector());
			assertTupleEquals(upVector, camera.getUpVector());
		}
	}
}
