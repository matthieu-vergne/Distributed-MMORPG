package fr.vergne.dmmorpg;

public interface Action<Target> {
	public Target apply(Target target);
}
