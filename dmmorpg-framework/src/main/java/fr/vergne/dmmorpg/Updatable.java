package fr.vergne.dmmorpg;

import fr.vergne.dmmorpg.Updatable.Update;

public interface Updatable<U extends Update<?, ?, ?>> {
	public void listenUpdate(Listener<? super U> listener);

	public void unlistenUpdate(Listener<? super U> listener);

	public static interface Update<Source, Property, Value> {
		public Source getSource();

		public Property getProperty();

		public Value getOldValue();

		public Value getNewValue();
	}

	public static interface Listener<U extends Update<?, ?, ?>> {
		public void updated(U update);
	}

	public static <U extends Update<?, ?, ?>> void fireUpdate(Iterable<Listener<? super U>> listeners, U update) {
		for (Listener<? super U> listener : listeners) {
			listener.updated(update);
		}
	}
}
