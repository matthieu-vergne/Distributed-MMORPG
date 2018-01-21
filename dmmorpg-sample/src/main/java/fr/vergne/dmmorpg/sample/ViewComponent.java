package fr.vergne.dmmorpg.sample;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.SwingUtilities;

import fr.vergne.dmmorpg.Updatable.Listener;
import fr.vergne.dmmorpg.sample.view.View;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;

@SuppressWarnings("serial")
public class ViewComponent extends Component {

	private View<Graphics> view;
	private boolean toRepaint = false;
	private final Listener<WorldUpdate> viewListener = update -> requestRepaint();

	public void setView(View<Graphics> view) {
		if (this.view == null) {
			// No listener to unsubscribe
		} else {
			view.unlistenUpdate(viewListener);
		}
		this.view = view;
		view.listenUpdate(viewListener);

		// addMouseListener(new MouseListener() {
		//
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// System.out.println("mouse clicked: (" + e.getX() + "," + e.getY() + ")");
		// }
		//
		// @Override
		// public void mouseReleased(MouseEvent e) {
		// // Not used
		// }
		//
		// @Override
		// public void mousePressed(MouseEvent e) {
		// // Not used
		// }
		//
		// @Override
		// public void mouseExited(MouseEvent e) {
		// // Not used
		// }
		//
		// @Override
		// public void mouseEntered(MouseEvent e) {
		// // Not used
		// }
		// });
		// addMouseMotionListener(new MouseMotionListener() {
		//
		// @Override
		// public void mouseMoved(MouseEvent e) {
		// System.out.println("mouse moved: (" + e.getX() + "," + e.getY() + ")");
		// }
		//
		// @Override
		// public void mouseDragged(MouseEvent e) {
		// System.out.println("mouse dragged: (" + e.getX() + "," + e.getY() + ")");
		// }
		// });
		// addMouseWheelListener(new MouseWheelListener() {
		//
		// @Override
		// public void mouseWheelMoved(MouseWheelEvent e) {
		// System.out.println("mouse wheel moved: " + e.getWheelRotation());
		// }
		// });
		// addPropertyChangeListener(new PropertyChangeListener() {
		//
		// @Override
		// public void propertyChange(PropertyChangeEvent evt) {
		// System.out.println("property changed: " + evt.getPropertyName() + "=" +
		// evt.getNewValue());
		// }
		// });
	}

	public View<Graphics> getView() {
		return view;
	}

	@Override
	public void paint(Graphics g) {
		if (view == null) {
			throw new IllegalStateException("No view set yet.");
		} else {
			view.render(g);
		}
	}

	private void requestRepaint() {
		if (!toRepaint) {
			toRepaint = true;
			SwingUtilities.invokeLater(() -> {
				repaint();
				toRepaint = false;
			});
		} else {
			// Repaint already requested
		}
	}
}
