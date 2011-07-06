package com.dmmorpg.camera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.junit.Test;

public class CameraTest {
	private static final double EPSILON = Double.parseDouble("1E-14");

	private static final void tupleEquals(Tuple3d expected, Tuple3d actual) {
		try {
			assertEquals(expected.x, actual.x, EPSILON);
			assertEquals(expected.y, actual.y, EPSILON);
			assertEquals(expected.z, actual.z, EPSILON);
		} catch (AssertionError ex) {
			fail("expected " + expected + " but was " + actual);
		}
	}

	@Test
	public void testCreation() {
		Camera camera = new Camera();
		tupleEquals(new Point3d(0, 0, 0), camera.getPosition());
		tupleEquals(new Vector3d(1, 0, 0), camera.getFrontVector());
		tupleEquals(new Vector3d(0, 1, 0), camera.getLeftVector());
		tupleEquals(new Vector3d(0, 0, 1), camera.getUpVector());
	}

	public static void assertNormalized(Vector3d vector) {
		assertEquals(1, vector.length(), EPSILON);
	}

	@Test
	public void testNormalizationOfAxis() {
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
	public void testOrthogonalityOfAxis() {
		Camera camera = new Camera();

		{
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.goFront(3.2);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.goBack(5);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.goLeft(6);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.goRight(2.5);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.goUp(112);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.goDown(12.3);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.lookUp(3.5);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.lookDown(53);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.lookLeft(64);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.lookRight(35.2);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.inclineLeft(12.5);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}

		{
			camera.inclineRight(95);
			Vector3d product = new Vector3d();
			product.cross(camera.getFrontVector(), camera.getLeftVector());
			tupleEquals(camera.getUpVector(), product);
		}
	}

	@Test
	public void testGoToPosition() {
		Camera camera = new Camera();
		Point3d position = new Point3d(camera.getPosition());
		position.x += 3;
		position.y += -5;
		position.z += 1;
		camera.goTo(position);
		tupleEquals(position, camera.getPosition());
		position.set(1, 2, 3);
		camera.goTo(position);
		tupleEquals(position, camera.getPosition());
	}

	@Test
	public void testGoToDirection() {
		Camera camera = new Camera();

		{
			camera.goTo(new Point3d(0, 0, 0));
			camera.goTo(new Vector3d(-3, 5, 4), 0);
			tupleEquals(new Point3d(0, 0, 0), camera.getPosition());
		}

		{
			camera.goTo(new Point3d(0, 0, 0));
			camera.goTo(new Vector3d(0, 0, 0), 1000);
			tupleEquals(new Point3d(0, 0, 0), camera.getPosition());
		}

		{
			camera.goTo(new Point3d(0, 0, 0));
			Vector3d delta = new Vector3d(-8.2, 2.5, 3);
			camera.goTo(delta, 1);
			tupleEquals(delta, camera.getPosition());
		}

		{
			camera.goTo(new Point3d(0, 0, 0));
			camera.goTo(new Vector3d(7, 5, 9), 10);
			tupleEquals(new Point3d(70, 50, 90), camera.getPosition());
		}
	}

	@Test
	public void testGoFront() {
		Camera camera = new Camera();
		double distance = 4.1;
		Vector3d movement = new Vector3d(camera.getFrontVector());
		movement.scale(distance);
		camera.goFront(distance);
		tupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoBack() {
		Camera camera = new Camera();
		double distance = 3.2;
		Vector3d movement = new Vector3d(camera.getFrontVector());
		movement.scale(-distance);
		camera.goBack(distance);
		tupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoLeft() {
		Camera camera = new Camera();
		double distance = 4.6;
		Vector3d movement = new Vector3d(camera.getLeftVector());
		movement.scale(distance);
		camera.goLeft(distance);
		tupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoRight() {
		Camera camera = new Camera();
		double distance = 1.2;
		Vector3d movement = new Vector3d(camera.getLeftVector());
		movement.scale(-distance);
		camera.goRight(distance);
		tupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoUp() {
		Camera camera = new Camera();
		double distance = 65.2;
		Vector3d movement = new Vector3d(camera.getUpVector());
		movement.scale(distance);
		camera.goUp(distance);
		tupleEquals(movement, camera.getPosition());
	}

	@Test
	public void testGoDown() {
		Camera camera = new Camera();
		double distance = 4;
		Vector3d movement = new Vector3d(camera.getUpVector());
		movement.scale(-distance);
		camera.goDown(distance);
		tupleEquals(movement, camera.getPosition());
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
		tupleEquals(frontVector, camera.getFrontVector());
		tupleEquals(leftVector, camera.getLeftVector());
		tupleEquals(upVector, camera.getUpVector());
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
		tupleEquals(frontVector, camera.getFrontVector());
		tupleEquals(leftVector, camera.getLeftVector());
		tupleEquals(upVector, camera.getUpVector());
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
		tupleEquals(frontVector, camera.getFrontVector());
		tupleEquals(leftVector, camera.getLeftVector());
		tupleEquals(upVector, camera.getUpVector());
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
		tupleEquals(frontVector, camera.getFrontVector());
		tupleEquals(leftVector, camera.getLeftVector());
		tupleEquals(upVector, camera.getUpVector());
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
		tupleEquals(frontVector, camera.getFrontVector());
		tupleEquals(leftVector, camera.getLeftVector());
		tupleEquals(upVector, camera.getUpVector());
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
		tupleEquals(frontVector, camera.getFrontVector());
		tupleEquals(leftVector, camera.getLeftVector());
		tupleEquals(upVector, camera.getUpVector());
	}
}
