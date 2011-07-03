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
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.dmmorpg.element.IElement;
import com.dmmorpg.element.Sphere;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Client extends Applet {
	private static final double stepSize = .05;
	private static final double angleSize = .0002;
	private static final long serialVersionUID = 1L;
	private final Robot mouseControler;
	private final Logger logger = Logger.getAnonymousLogger();
	private final Label foot = new Label("-");
	private final SimpleUniverse universe;
	private float userX = -2;
	private float userY = 0;
	private float userZ = 0;
	private float YAngle = 0;
	private float ZAngle = 0;
	private final Collection<IElement> elements = new HashSet<IElement>();

	public Client() {
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
		sphere.putInBranchGroup(objRoot);
		elements.add(sphere);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);
		Color3f light1Color = new Color3f(0.0f, 3.0f, 0.0f);
		Vector3f light1Direction = new Vector3f(1, -1, -1);
		DirectionalLight light1 = new DirectionalLight(light1Color,
				light1Direction);
		light1.setInfluencingBounds(bounds);
		objRoot.addChild(light1);

		// Régler la lumière ambiante
		Color3f ambientColor = new Color3f(0.0f, 0.0f, 0.0f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setBounds(bounds);
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
				// do nothing
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// do nothing
			}

			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				switch (key) {
				case KeyEvent.VK_RIGHT:
					logger.info("go right");
					userY -= stepSize;
					break;
				case KeyEvent.VK_LEFT:
					logger.info("go left");
					userY += stepSize;
					break;
				case KeyEvent.VK_UP:
					logger.info("go up");
					userX += stepSize;
					break;
				case KeyEvent.VK_DOWN:
					logger.info("go down");
					userX -= stepSize;
					break;
				default:
					logger.warning("unused key : " + key);
					return;
				}
				updateCamera();
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
				int deltaY = newY - originY;
				mouseControler.mouseMove(originX, originY);

				YAngle -= (float) deltaY * angleSize;
				ZAngle -= (float) deltaX * angleSize;
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
		t3d.lookAt(new Point3d(userX, userY, userZ), new Point3d(userX + Math.cos(YAngle)*Math.cos(ZAngle),
				userY+Math.sin(ZAngle), userZ + Math.sin(YAngle)), new Vector3d(0, 0, 1));
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
		new MainFrame(client, 600, 600);
	}
}