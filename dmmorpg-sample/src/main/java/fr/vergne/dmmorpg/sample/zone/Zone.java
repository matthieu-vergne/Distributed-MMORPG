package fr.vergne.dmmorpg.sample.zone;

import java.io.File;

import fr.vergne.dmmorpg.sample.world.WorldPosition;

public interface Zone {
	public Type getType(WorldPosition position);

	public static class Type {
		private final File descriptor;

		public Type(File descriptor) {
			this.descriptor = descriptor;
		}

		public File getDescriptor() {
			return descriptor;
		}
	}
}
