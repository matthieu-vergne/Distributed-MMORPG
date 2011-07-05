package com.dmmorpg.camera;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class Camera {
	private static final double STEP_SPEED = .05;
	private final Logger logger = Logger.getAnonymousLogger();
	private Point3d position = new Point3d();
	private Vector3d frontVector = new Vector3d(1, 0, 0);
	private Vector3d upVector = new Vector3d(0, 0, 1);

	public Camera() {
		logger.setLevel(Level.WARNING);
	}

	public void goRight() {
		Tuple3d movement = new Vector3d();
		movement.scale(-STEP_SPEED, getLeftVector());
		logger.info("Movement : " + movement);
		getPosition().add(movement);
	}

	public void goLeft() {
		Tuple3d movement = new Vector3d();
		movement.scale(STEP_SPEED, getLeftVector());
		logger.info("Movement : " + movement);
		getPosition().add(movement);
	}

	public void goBack() {
		Tuple3d movement = new Vector3d();
		movement.scale(-STEP_SPEED, getFrontVector());
		logger.info("Movement : " + movement);
		getPosition().add(movement);
	}

	public void goFront() {
		Tuple3d movement = new Vector3d();
		movement.scale(STEP_SPEED, getFrontVector());
		logger.info("Movement : " + movement);
		getPosition().add(movement);
	}

	public void lookUp(double angle) {
		logger.info("target angle : " + angle);
		
		Vector3d newFrontVector = new Vector3d();
		{
			Vector3d frontPart = new Vector3d(frontVector);
			frontPart.scale(Math.cos(angle));
			
			Vector3d upPart = new Vector3d(upVector);
			upPart.scale(Math.sin(angle));
			
			newFrontVector.add(frontPart);
			newFrontVector.add(upPart);
			newFrontVector.normalize();
		}

		Vector3d newUpVector = new Vector3d();
		{
			Vector3d upPart = new Vector3d(upVector);
			upPart.scale(Math.cos(angle));
			
			Vector3d backPart = new Vector3d(frontVector);
			backPart.negate();
			backPart.scale(Math.sin(angle));
			
			newUpVector.add(backPart);
			newUpVector.add(upPart);
			newUpVector.normalize();
		}

		logger.info("target front angle : " + frontVector.angle(newFrontVector));
		frontVector.set(newFrontVector);
		logger.info("target up angle : " + upVector.angle(newUpVector));
		upVector.set(newUpVector);
	}

	public void lookLeft(double angle) {
		logger.info("target angle : " + angle);
		
		Vector3d newFrontVector = new Vector3d();
		{
			Vector3d frontPart = new Vector3d(frontVector);
			frontPart.scale(Math.cos(angle));
			
			Vector3d leftPart = new Vector3d(getLeftVector());
			leftPart.scale(Math.sin(angle));
			
			newFrontVector.add(frontPart);
			newFrontVector.add(leftPart);
			newFrontVector.normalize();
		}

		logger.info("target front angle : " + frontVector.angle(newFrontVector));
		frontVector.set(newFrontVector);
	}

	public Point3d getPosition() {
		return position;
	}

	public Vector3d getFrontVector() {
		return frontVector;
	}

	public Vector3d getLeftVector() {
		Vector3d leftVector = new Vector3d();
		leftVector.cross(upVector, frontVector);
		leftVector.normalize();
		return leftVector;
	}

	public Vector3d getUpVector() {
		return upVector;
	}

}
