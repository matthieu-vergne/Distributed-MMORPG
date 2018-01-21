package fr.vergne.dmmorpg.sample.world;

import fr.vergne.dmmorpg.sample.Direction;

public interface AccessPolicy<T> {

	public boolean canEnter(T t, Direction side);

	public boolean canLeave(T t, Direction side);

	public static AccessPolicy<Object> ALLOW_ALL = new AccessPolicy<Object>() {

		@Override
		public boolean canEnter(Object t, Direction side) {
			return true;
		}

		@Override
		public boolean canLeave(Object t, Direction side) {
			return true;
		}
	};

	public static AccessPolicy<Object> BLOCK_ALL = new AccessPolicy<Object>() {

		@Override
		public boolean canEnter(Object t, Direction side) {
			return false;
		}

		@Override
		public boolean canLeave(Object t, Direction side) {
			return false;
		}
	};
}
