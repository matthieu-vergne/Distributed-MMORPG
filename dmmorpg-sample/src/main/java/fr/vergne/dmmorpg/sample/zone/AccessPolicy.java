package fr.vergne.dmmorpg.sample.zone;

import fr.vergne.dmmorpg.sample.Direction;

public interface AccessPolicy<T> {

	public boolean canEnter(T t, Direction direction);

	public boolean canLeave(T t, Direction direction);

	public static AccessPolicy<Object> ALLOW_ALL = new AccessPolicy<Object>() {

		@Override
		public boolean canEnter(Object t, Direction direction) {
			return true;
		}

		@Override
		public boolean canLeave(Object t, Direction direction) {
			return true;
		}
	};

	public static AccessPolicy<Object> BLOCK_ALL = new AccessPolicy<Object>() {

		@Override
		public boolean canEnter(Object t, Direction direction) {
			return false;
		}

		@Override
		public boolean canLeave(Object t, Direction direction) {
			return false;
		}
	};
}
