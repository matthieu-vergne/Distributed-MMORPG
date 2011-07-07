package com.dmmorpg.camera;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

/**
 * The camera is a device which allows to have a simple way to view the
 * universe.
 */
public class Camera {
	private static final double EPSILON = Double.parseDouble("1E-14");
	public static final Logger logger = Logger.getAnonymousLogger();
	private Point3d position = new Point3d();
	private Vector3d frontVector = new Vector3d(1, 0, 0);
	private Vector3d leftVector = new Vector3d(0, 1, 0);
	private Vector3d upVector = new Vector3d(0, 0, 1);

	/**
	 * Create a new camera, positioned at (0,0,0), looking along the X axis and
	 * with the altitude along the Z axis.
	 */
	public Camera() {
		logger.setLevel(Level.WARNING);
	}

	/**
	 * Ask to the camera to go to a relative position through a direction. If
	 * the direction is normalized, the scale is equivalent to the distance to
	 * travel following this direction, otherwise it is a simple coefficient.
	 * 
	 * @param direction
	 *            the direction to follow
	 * @param scale
	 *            the scale to apply to this direction
	 */
	public void goTo(Vector3d direction, double scale) {
		Tuple3d movement = new Vector3d();
		movement.scale(scale, direction);
		logger.info("Movement : " + movement);
		position.add(movement);
	}

	/**
	 * Ask to the camera to go left.
	 * 
	 * @param distance
	 *            the distance to travel
	 */
	public void goLeft(double distance) {
		goTo(getLeftVector(), distance);
	}

	/**
	 * Ask to the camera to go right.
	 * 
	 * @param distance
	 *            the distance to travel
	 */
	public void goRight(double distance) {
		goLeft(-distance);
	}

	/**
	 * Ask to the camera to go front.
	 * 
	 * @param distance
	 *            the distance to travel
	 */
	public void goFront(double distance) {
		goTo(getFrontVector(), distance);
	}

	/**
	 * Ask to the camera to go back.
	 * 
	 * @param distance
	 *            the distance to travel
	 */
	public void goBack(double distance) {
		goFront(-distance);
	}

	/**
	 * Ask to the camera to go up.
	 * 
	 * @param distance
	 *            the distance to travel
	 */
	public void goUp(double distance) {
		goTo(getUpVector(), distance);
	}

	/**
	 * Ask to the camera to go down.
	 * 
	 * @param distance
	 *            the distance to travel
	 */
	public void goDown(double distance) {
		goUp(-distance);
	}

	/**
	 * Ask the camera to turn around an axis
	 * 
	 * @param axis
	 * @param angle
	 */
	public void turnOn(Vector3d axis, double angle) {
		if (axis.length() == 0) {
			throw new NullVectorException();
		}

		Vector3d firstVector;
		Vector3d secondVector;
		if (axis == leftVector) {
			firstVector = frontVector;
			secondVector = upVector;
		} else if (axis == upVector) {
			firstVector = frontVector;
			secondVector = leftVector;
		} else if (axis == frontVector) {
			firstVector = upVector;
			secondVector = leftVector;
		} else {
			// TODO implement the possibility to turn on a custom axis
			throw new IllegalArgumentException(
					"the case the direction is not a known axis vector is not implemented");
		}

		Vector3d newFirstVector = new Vector3d();
		{
			Vector3d firstPart = new Vector3d(firstVector);
			firstPart.scale(Math.cos(angle));

			Vector3d secondPart = new Vector3d(secondVector);
			secondPart.scale(Math.sin(angle));

			newFirstVector.add(firstPart);
			newFirstVector.add(secondPart);
			newFirstVector.normalize();
		}

		Vector3d newSecondVector = new Vector3d();
		{
			Vector3d firstPart = new Vector3d(firstVector);
			firstPart.negate();
			firstPart.scale(Math.sin(angle));

			Vector3d secondPart = new Vector3d(secondVector);
			secondPart.scale(Math.cos(angle));

			newSecondVector.add(firstPart);
			newSecondVector.add(secondPart);
			newSecondVector.normalize();
		}

		logger.info("first vector : " + firstVector + " -> " + newFirstVector
				+ "\n" + "second vector : " + secondVector + " -> "
				+ newSecondVector);

		firstVector.set(newFirstVector);
		secondVector.set(newSecondVector);
	}

	/**
	 * Ask to the camera to look up.
	 * 
	 * @param angle
	 *            the angle to look
	 */
	public void lookUp(double angle) {
		turnOn(leftVector, angle);
	}

	/**
	 * Ask to the camera to look down.
	 * 
	 * @param angle
	 *            the angle to look
	 */
	public void lookDown(double angle) {
		lookUp(-angle);
	}

	/**
	 * Ask to the camera to look left.
	 * 
	 * @param angle
	 *            the angle to look
	 */
	public void lookLeft(double angle) {
		turnOn(upVector, angle);
	}

	/**
	 * Ask to the camera to look right.
	 * 
	 * @param angle
	 *            the angle to look
	 */
	public void lookRight(double angle) {
		lookLeft(-angle);
	}

	/**
	 * Ask to the camera to incline the view on the left.
	 * 
	 * @param angle
	 *            the angle to incline
	 */
	public void inclineLeft(double angle) {
		turnOn(frontVector, angle);
	}

	/**
	 * Ask to the camera to incline the view on the right.
	 * 
	 * @param angle
	 *            the angle to incline
	 */
	public void inclineRight(double angle) {
		inclineLeft(-angle);
	}

	/**
	 * 
	 * @param position
	 *            the new position of the camera
	 */
	public void setPosition(Point3d position) {
		this.position.set(position);
	}

	/**
	 * 
	 * @return the actual position of the camera
	 */
	public Point3d getPosition() {
		return position;
	}

	/**
	 * Apply a specific orientation to the camera. The X and Z axis are needed,
	 * the Y axis is calculated. The X and Z axis must be orthogonal, but they
	 * do not need to be normalized (all the axis are explicitly normalized in
	 * this method).
	 * 
	 * @param frontVector
	 *            the local X axis
	 * @param upVector
	 *            the local Z axis
	 */
	public void setOrientation(Vector3d frontVector, Vector3d upVector) {
		if (frontVector.length() == 0 || upVector.length() == 0) {
			throw new NullVectorException();
		}
		if (frontVector.dot(upVector) > EPSILON) {
			throw new NotOrthogonalVectorsException();
		}
		
		this.frontVector.set(frontVector);
		this.frontVector.normalize();
		this.upVector.set(upVector);
		this.upVector.normalize();
		this.leftVector.cross(upVector, frontVector);
		this.leftVector.normalize();
	}

	/**
	 * 
	 * @return a normalized vector giving the direction where the camera look
	 */
	public Vector3d getFrontVector() {
		return frontVector;
	}

	/**
	 * 
	 * @return a normalized vector giving the left of the camera
	 */
	public Vector3d getLeftVector() {
		return leftVector;
	}

	/**
	 * 
	 * @return a normalized vector telling where is the top of the camera
	 */
	public Vector3d getUpVector() {
		return upVector;
	}
}
