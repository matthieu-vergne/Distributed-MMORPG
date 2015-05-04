package fr.vergne.dmmorpgSample.universe.habitat;

import fr.vergne.dmmorpgSample.universe.animal.Animal;

/**
 * An {@link Habitat} is an area where {@link Animal}s live.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface Habitat {

	/**
	 * A {@link Position} indicates a specific place within an {@link Habitat}.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 */
	public interface Position {
		/**
		 * 
		 * @return the horizontal location in the {@link Habitat}
		 */
		public double getX();

		/**
		 * The height of an {@link Habitat} having a top limit given by the
		 * water limit, the vertical location evolves inversely to the depth,
		 * thus zero for the water limit and a decreasing (negative) value by
		 * going deeper.
		 * 
		 * @return the vertical location in the {@link Habitat}
		 */
		public double getY();
	}
}
