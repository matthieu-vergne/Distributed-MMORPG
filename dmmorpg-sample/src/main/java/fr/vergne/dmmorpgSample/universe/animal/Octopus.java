package fr.vergne.dmmorpgSample.universe.animal;

/**
 * An {@link Octopus} is a playable {@link Animal}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface Octopus extends Animal {

	/**
	 * The arms length reflect the amount of life remaining. Thus, the average
	 * length corresponds to the ratio between {@link Animal#getRemainingLife()}
	 * and {@link Animal#getLifeLimit()}. When injured, life decreases with some
	 * arms, while healing allows to recover life and grow incomplete arms.
	 * 
	 * @param armIndex
	 *            the index of the arm (1-8)
	 * @return the current length of the arm in [0;1], one for a complete arm
	 *         and zero for no arm at all
	 */
	public double getArmLength(int armIndex);
}
