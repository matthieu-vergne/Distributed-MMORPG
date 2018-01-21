package fr.vergne.dmmorpg.sample.view;

import java.util.Arrays;

public class ViewPosition {

	private final int x;
	private final int y;

	public ViewPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return Arrays.asList(x, y).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof ViewPosition) {
			ViewPosition p = (ViewPosition) obj;
			return p.x == x && p.y == y;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (Math.abs(x) + 1) * (Math.abs(y) + 1);
	}
}
