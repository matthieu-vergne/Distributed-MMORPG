package fr.vergne.dmmorpg;

public interface Renderer<T, Graphics> {
	public void render(T t, Graphics graphics);
}
