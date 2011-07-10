package com.dmmorpg;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Label;
import java.awt.Panel;
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

import com.dmmorpg.camera.Camera;
import com.dmmorpg.element.I3DElement;
import com.dmmorpg.element.Sphere;
import com.dmmorpg.util.FormatUtil;
import com.dmmorpg.util.MouseUtil;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Client extends Applet {
	public static final Logger logger = Logger.getAnonymousLogger();
	private static final double ANGLE_SPEED = .0005;
	private static final int WINDOW_WIDTH = Toolkit.getDefaultToolkit()
			.getScreenSize().width;
	private static final int WINDOW_HEIGHT = 500;
	private static final long serialVersionUID = 1L;
	private static final double STEP_UNIT = .05;
	private final Label coord = new Label("-");
	private final Label angle = new Label("-");
	private final SimpleUniverse universe;
	private final Collection<I3DElement> elements = new HashSet<I3DElement>();
	private boolean goingFront = false;
	private boolean goingBack = false;
	private boolean goingLeft = false;
	private boolean goingRight = false;
	private boolean goingUp = false;
	private boolean goingDown = false;
	private boolean inclineLeft = false;
	private boolean inclineRight = false;
	private Camera camera = new Camera();

	public Client() {
		logger.setLevel(Level.WARNING);

		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);
		add("Center", c);
		Panel p = new Panel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(coord);
		p.add(angle);
		add("South", p);

		BranchGroup scene = createSceneGraph();
		universe = new SimpleUniverse(c);
		universe.addBranchGraph(scene);

		configListeners();
	}

	public BranchGroup createSceneGraph() {
		BranchGroup objRoot = new BranchGroup();

		Sphere lampadaire = new Sphere();
		lampadaire.setPosition(5, 0, 0);
		lampadaire.setBranchGroup(objRoot);
		elements.add(lampadaire);

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
				refreshView();
				refreshInterface();
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
				} else if (key == KeyEvent.VK_HOME) {
					setGoingUp(false);
				} else if (key == KeyEvent.VK_END) {
					setGoingDown(false);
				} else if (key == KeyEvent.VK_PAGE_UP) {
					setInclineLeft(false);
				} else if (key == KeyEvent.VK_PAGE_DOWN) {
					setInclineRight(false);
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
				} else if (key == KeyEvent.VK_HOME) {
					setGoingUp(true);
				} else if (key == KeyEvent.VK_END) {
					setGoingDown(true);
				} else if (key == KeyEvent.VK_PAGE_UP) {
					setInclineLeft(true);
				} else if (key == KeyEvent.VK_PAGE_DOWN) {
					setInclineRight(true);
				} else {
					logger.warning("unused key : " + key);
				}
			}

		});

		universe.getCanvas().addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Dimension windowSize = ((Canvas3D) e.getSource()).getSize();
				int originX = windowSize.width / 2;
				int originY = windowSize.height / 2;
				int newX = e.getX();
				int newY = e.getY();

				if (newX == originX && newY == originY) {
					return;
				}

				int deltaX = newX - originX;
				int deltaY = newY - originY;
				MouseUtil.setPositionOnScreen(e.getXOnScreen() - deltaX, e.getYOnScreen() - deltaY);

				if (deltaX != 0) {
					camera.lookRight(deltaX * ANGLE_SPEED);
				}
				if (deltaY != 0) {
					camera.lookDown(deltaY * ANGLE_SPEED);
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// do nothing
			}
		});
	}

	private void refreshView() {
		if (isGoingFront()) {
			camera.goFront(STEP_UNIT);
		} else if (isGoingBack()) {
			camera.goBack(STEP_UNIT);
		}

		if (isGoingLeft()) {
			camera.goLeft(STEP_UNIT);
		} else if (isGoingRight()) {
			camera.goRight(STEP_UNIT);
		}

		if (isGoingUp()) {
			camera.goUp(STEP_UNIT);
		} else if (isGoingDown()) {
			camera.goDown(STEP_UNIT);
		}

		if (isInclineLeft()) {
			camera.inclineLeft(STEP_UNIT);
		} else if (isInclineRight()) {
			camera.inclineRight(STEP_UNIT);
		}

		TransformGroup steerTG = universe.getViewingPlatform()
				.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);
		Tuple3d target = new Vector3d();
		Point3d cameraPosition = camera.getPosition();
		target.add(cameraPosition);
		target.add(camera.getFrontVector());
		t3d.lookAt(cameraPosition, new Point3d(target), camera.getUpVector());
		t3d.invert();
		steerTG.setTransform(t3d);
	}

	private void refreshInterface() {
		TransformGroup steerTG = universe.getViewingPlatform()
				.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);
		Matrix3f rotation = new Matrix3f();
		Vector3f translation = new Vector3f();
		t3d.get(rotation, translation);
		String rot0 = FormatUtil.concat(" | ", "" + rotation.m00, ""
				+ rotation.m01, "" + rotation.m02);
		String rot1 = FormatUtil.concat(" | ", "" + rotation.m10, ""
				+ rotation.m11, "" + rotation.m12);
		String rot2 = FormatUtil.concat(" | ", "" + rotation.m20, ""
				+ rotation.m21, "" + rotation.m22);
		String rot = "[" + FormatUtil.concat("] [", rot0, rot1, rot2) + "]";
		coord.setText(translation.toString());
		angle.setText(rot);
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

	public void setGoingUp(boolean goingUp) {
		this.goingUp = goingUp;
	}

	public boolean isGoingUp() {
		return goingUp;
	}

	public void setGoingDown(boolean goingDown) {
		this.goingDown = goingDown;
	}

	public boolean isGoingDown() {
		return goingDown;
	}

	public void setInclineLeft(boolean inclineLeft) {
		this.inclineLeft = inclineLeft;
	}

	public boolean isInclineLeft() {
		return inclineLeft;
	}

	public void setInclineRight(boolean inclineRight) {
		this.inclineRight = inclineRight;
	}

	public boolean isInclineRight() {
		return inclineRight;
	}

}