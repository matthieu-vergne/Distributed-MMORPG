package fr.vergne.dmmorpgSample.universe.animal;

import java.util.Collection;

import fr.vergne.dmmorpgSample.universe.activity.Activity;
import fr.vergne.dmmorpgSample.universe.habitat.Habitat;
import fr.vergne.dmmorpgSample.universe.habitat.Habitat.Position;

/**
 * An {@link Animal} is a creature able to act in an {@link Habitat}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface Animal {

	/**
	 * 
	 * @return the current {@link Habitat} in which this {@link Animal} evolves
	 */
	public Habitat getHabitat();

	/**
	 * 
	 * @return the current {@link Position} of the {@link Animal} in the
	 *         {@link Habitat}
	 */
	public Position getPosition();

	/**
	 * 
	 * @return the current running {@link Activity}s of the {@link Animal}
	 */
	public Collection<Activity> getActivities();

	/**
	 * 
	 * @return the amount of life remaining for this {@link Animal}
	 */
	public double getRemainingLife();

	/**
	 * 
	 * @return the maximal amount of life this {@link Animal} can have
	 */
	public double getLifeLimit();

	/**
	 * 
	 * @param listener
	 *            the {@link AnimalListener} to register to get notifications
	 *            about this {@link Animal}
	 */
	public void regiser(AnimalListener listener);

	/**
	 * An {@link AnimalListener} allows to be notified when some properties of
	 * an {@link Animal} change.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 */
	public interface AnimalListener {
		/**
		 * 
		 * @param lifeReduction
		 *            how much life has just been removed
		 */
		public void injure(int lifeReduction);

		/**
		 * 
		 * @param lifeIncrease
		 *            how much life has just been recovered
		 */
		public void heal(int lifeIncrease);

		/**
		 * 
		 * @param activity
		 *            which {@link Activity} the {@link Animal} has just started
		 */
		public void startActivity(Activity activity);

		/**
		 * 
		 * @param activity
		 *            which {@link Activity} the {@link Animal} has just stopped
		 */
		public void stopActivity(Activity activity);

		/**
		 * 
		 * @param activity
		 *            which {@link Activity} the {@link Animal} has just
		 *            finished
		 */
		public void finishActivity(Activity activity);
	}
}
