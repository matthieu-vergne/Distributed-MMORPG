package com.dmmorpg.util;

/**
 * This class offers several methods to do some classical checks.
 * 
 * @author Matthieu VERGNE <matthieu.vergne@gmail.com>
 * 
 */
public abstract class CheckUtil {

	/**
	 * Check the given strings are not null nor empty ("").
	 * 
	 * @param strings
	 *            the strings to check
	 * @return true if at least one of them is null or empty
	 */
	public static boolean isNullOrEmpty(String... strings) {
		for (String str : strings) {
			if (str == null || str.length() == 0) {
				return true;
			}
		}
		return false;
	}
}
