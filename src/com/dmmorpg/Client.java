package com.dmmorpg;

import java.applet.Applet;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.BoxLayout;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.dmmorpg.element.IElement;
import com.dmmorpg.element.Sphere;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Client extends Applet {
	private static final double STEP_SIZE = .05;
	private static final double ANGLE_SIZE = .0005;
	private static final int WINDOW_WIDTH = Toolkit.getDefaultToolkit()
			.getScreenSize().width;
	private static final int WINDOW_HEIGHT = 500;
	private static final long serialVersionUID = 1L;
	private final Robot mouseControler;
	private final Logger logger = Logger.getAnonymousLogger();
	private final Label foot = new Label("-");
	private final SimpleUniverse universe;
	private Vector3d position = new Vector3d(0, 0, 0);
	private Vector3d direction = new Vector3d(1, 0, 0);
	private Vector3d upVector = new Vector3d(0, 0, 1);
	private final Collection<IElement> elements = new HashSet<IElement>();
	private boolean goingFront = false;
	private boolean goingBack = false;
	private boolean goingLeft = false;
	private boolean goingRight = false;

	public Client() {
		logger.setLevel(Level.WARNING);
		try {
			mouseControler = new Robot();
		} catch (AWTException e) {
			throw new IllegalStateException(e);
		}

		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);
		add("Center", c);
		Panel p = new Panel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(foot);
		add("South", p);

		// Créer une scène simple et l’ajouter à l’univers
		BranchGroup scene = createSceneGraph();
		universe = new SimpleUniverse(c);
		universe.addBranchGraph(scene);
		updateCamera();

		configListeners();
	}

	public BranchGroup createSceneGraph() {
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();

		// Créer une forme simple et l’ajouter au graphe de scène
		Sphere sphere = new Sphere(0.25f) {
			@Override
			protected boolean hasChanged() {
				return false;
			}
		};
		sphere.setPosition(2, 0, 0);
		sphere.putInBranchGroup(objRoot);
		elements.add(sphere);
		sphere = new Sphere(0.25f) {
			@Override
			protected boolean hasChanged() {
				return false;
			}
		};
		sphere.setPosition(0, 2, 0);
		sphere.putInBranchGroup(objRoot);
		elements.add(sphere);
		sphere = new Sphere(0.25f) {
			@Override
			protected boolean hasChanged() {
				return false;
			}
		};
		sphere.setPosition(-2, 0, 0);
		sphere.putInBranchGroup(objRoot);
		elements.add(sphere);
		sphere = new Sphere(0.25f) {
			@Override
			protected boolean hasChanged() {
				return false;
			}
		};
		sphere.setPosition(0, -2, 0);
		sphere.putInBranchGroup(objRoot);
		elements.add(sphere);
		sphere = new Sphere(0.25f) {
			@Override
			protected boolean hasChanged() {
				return false;
			}
		};
		sphere.setPosition(0, 0, 2);
		sphere.putInBranchGroup(objRoot);
		elements.add(sphere);
		sphere = new Sphere(0.25f) {
			@Override
			protected boolean hasChanged() {
				return false;
			}
		};
		sphere.setPosition(0, 0, -2);
		sphere.putInBranchGroup(objRoot);
		elements.add(sphere);

		LineArray axisX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisX.setCoordinate(0, new Point3d(-2, 0, 0));
		axisX.setCoordinate(1, new Point3d(2, 0, 0));
		axisX.setColor(0, new Color3f(1f, 1f, 1f));
		axisX.setColor(1, new Color3f(1f, 1f, 1f));
		objRoot.addChild(new Shape3D(axisX));

		LineArray axisY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisY.setCoordinate(0, new Point3d(0, -2, 0));
		axisY.setCoordinate(1, new Point3d(0, 2, 0));
		axisY.setColor(0, new Color3f(1f, 1f, 1f));
		axisY.setColor(1, new Color3f(1f, 1f, 1f));
		objRoot.addChild(new Shape3D(axisY));

		LineArray axisZ = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisZ.setCoordinate(0, new Point3d(0, 0, -2));
		axisZ.setCoordinate(1, new Point3d(0, 0, 2));
		axisZ.setColor(0, new Color3f(1f, 1f, 1f));
		axisZ.setColor(1, new Color3f(1f, 1f, 1f));
		objRoot.addChild(new Shape3D(axisZ));

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);
		Color3f greenColor = new Color3f(0.0f, 3.0f, 0.0f);
		Vector3f lightDirection = new Vector3f(1, -1, -1);
		DirectionalLight light1 = new DirectionalLight(greenColor,
				lightDirection);
		light1.setInfluencingBounds(bounds);
		objRoot.addChild(light1);

		Color3f redColor = new Color3f(3.0f, 0.0f, 0.0f);
		lightDirection.negate();
		DirectionalLight light2 = new DirectionalLight(redColor, lightDirection);
		light2.setInfluencingBounds(bounds);
		objRoot.addChild(light2);

		// Régler la lumière ambiante
		Color3f ambientColor = new Color3f(100000f, 0.0f, 0.0f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(bounds);
		objRoot.addChild(ambientLightNode);

		return objRoot;
	}

	private void configListeners() {
		new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refreshUniverse();
			}
		}).start();

		universe.getCanvas().addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_UP) {
					setGoingFront(false);
				} else if (key == KeyEvent.VK_DOWN) {
					setGoingBack(false);
				} else if (key == KeyEvent.VK_LEFT) {
					setGoingLeft(false);
				} else if (key == KeyEvent.VK_RIGHT) {
					setGoingRight(false);
				} else {
					logger.warning("unused key : " + key);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_UP) {
					setGoingFront(true);
				} else if (key == KeyEvent.VK_DOWN) {
					setGoingBack(true);
				} else if (key == KeyEvent.VK_LEFT) {
					setGoingLeft(true);
				} else if (key == KeyEvent.VK_RIGHT) {
					setGoingRight(true);
				} else {
					logger.warning("unused key : " + key);
				}
			}

		});

		universe.getCanvas().addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Dimension screenSize = Toolkit.getDefaultToolkit()
						.getScreenSize();
				int originX = screenSize.width / 2;
				int originY = screenSize.height / 2;
				int newX = e.getXOnScreen();
				int newY = e.getYOnScreen();

				if (newX == originX && newY == originY) {
					return;
				}

				int deltaX = newX - originX;
				int deltaY = originY - newY;
				mouseControler.mouseMove(originX, originY);

				if (deltaX != 0) {
					Vector3d horizontalDirection = new Vector3d(direction);
					horizontalDirection.cross(direction, upVector);
					horizontalDirection.normalize();
					horizontalDirection.scale(Math.sin(deltaX * ANGLE_SIZE));
					Vector3d finalDirection = new Vector3d();
					finalDirection.scaleAdd(Math.cos(deltaX * ANGLE_SIZE),
							direction, horizontalDirection);
					finalDirection.normalize();

					direction.set(finalDirection);
				}
				if (deltaY != 0) {
					Vector3d verticalDirection = new Vector3d(upVector);
					verticalDirection.scale(Math.sin(deltaY * ANGLE_SIZE));
					Vector3d finalDirection = new Vector3d();
					finalDirection.scaleAdd(Math.cos(deltaY * ANGLE_SIZE),
							direction, verticalDirection);
					finalDirection.normalize();

					Vector3d backDirection = new Vector3d(direction);
					backDirection.scale(Math.sin(-deltaY * ANGLE_SIZE));
					Vector3d finalUpVector = new Vector3d();
					finalUpVector.scaleAdd(Math.cos(deltaY * ANGLE_SIZE),
							upVector, backDirection);
					finalUpVector.normalize();

					direction.set(finalDirection);
					upVector.set(finalUpVector);
				}
				updateCamera();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// do nothing
			}
		});
	}

	private void updateCamera() {
		TransformGroup steerTG = universe.getViewingPlatform()
				.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);
		Tuple3d target = new Vector3d();
		target.add(position);
		target.add(direction);
		t3d.lookAt(new Point3d(position), new Point3d(target), upVector);
		t3d.invert();
		steerTG.setTransform(t3d);

		/*
		 * Matrix3f m1 = new Matrix3f(); Vector3f t1 = new Vector3f();
		 * t3d.get(m1, t1); System.out.println(m1); System.out.println(t1);
		 */
	}

	private void refreshUniverse() {
		for (IElement element : elements) {
			element.updateInUniverse();
		}

		if (isGoingFront()) {
			goFront();
		}
		else if (isGoingBack()) {
			goBack();
		}
		
		if (isGoingLeft()) {
			goLeft();
		}
		else if (isGoingRight()) {
			goRight();
		}

		TransformGroup steerTG = universe.getViewingPlatform()
				.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);
		Matrix3f m1 = new Matrix3f();
		Vector3f t1 = new Vector3f();
		t3d.get(m1, t1);
		foot.setText(t1.toString());
	}

	public static void main(String[] args) {
		System.out.println("Program Started");
		Client client = new Client();
		new MainFrame(client, WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void setGoingFront(boolean goingFront) {
		this.goingFront = goingFront;
	}

	public boolean isGoingFront() {
		return goingFront;
	}

	public void setGoingBack(boolean goingBack) {
		this.goingBack = goingBack;
	}

	public boolean isGoingBack() {
		return goingBack;
	}

	public void setGoingLeft(boolean goingLeft) {
		this.goingLeft = goingLeft;
	}

	public boolean isGoingLeft() {
		return goingLeft;
	}

	public void setGoingRight(boolean goingRight) {
		this.goingRight = goingRight;
	}

	public boolean isGoingRight() {
		return goingRight;
	}

	private void goRight() {
		logger.info("go right");
		Vector3d movementDirection = new Vector3d();
		movementDirection.cross(direction, upVector);
		movementDirection.normalize();
		Tuple3d movement = new Vector3d();
		movement.scale(STEP_SIZE, movementDirection);
		logger.info("Movement : " + movement);
		position.add(movement);
		updateCamera();
	}

	private void goLeft() {
		logger.info("go left");
		Vector3d movementDirection = new Vector3d();
		movementDirection.cross(upVector, direction);
		movementDirection.normalize();
		Tuple3d movement = new Vector3d();
		movement.scale(STEP_SIZE, movementDirection);
		logger.info("Movement : " + movement);
		position.add(movement);
		updateCamera();
	}

	private void goBack() {
		logger.info("go back");
		Vector3d movementDirection = new Vector3d();
		movementDirection.negate(direction);
		Tuple3d movement = new Vector3d();
		movement.scale(STEP_SIZE, movementDirection);
		logger.info("Movement : " + movement);
		position.add(movement);
		updateCamera();
	}

	private void goFront() {
		logger.info("go front");
		Vector3d movementDirection = new Vector3d();
		movementDirection.set(direction);
		Tuple3d movement = new Vector3d();
		movement.scale(STEP_SIZE, movementDirection);
		logger.info("Movement : " + movement);
		position.add(movement);
		updateCamera();
	}
}