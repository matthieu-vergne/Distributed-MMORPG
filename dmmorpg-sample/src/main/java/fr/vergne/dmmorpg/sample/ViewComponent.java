package fr.vergne.dmmorpg.sample;

import java.awt.Component;
import java.awt.Graphics;

import fr.vergne.dmmorpg.sample.view.View;

@SuppressWarnings("serial")
public class ViewComponent extends Component {

	private View<Graphics> view;

	public void setView(View<Graphics> view) {
		this.view = view;
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
}
