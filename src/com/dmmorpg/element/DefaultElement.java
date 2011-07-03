package com.dmmorpg.element;


public abstract class DefaultElement implements IElement {
	abstract protected boolean hasChanged();

	abstract protected void redraw();

	@Override
	public void updateInUniverse() {
		if (hasChanged()) {
			redraw();
		}
	}
}
