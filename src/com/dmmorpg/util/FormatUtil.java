package com.dmmorpg.util;

/**
 * Several useful formatting methods.
 * 
 * @author Matthieu VERGNE <matthieu.vergne@gmail.com>
 * 
 */
public abstract class FormatUtil {

	/**
	 * Concat several strings in a homogeneous enumeration. If null elements are
	 * given, they are considered as empty strings ("").
	 * 
	 * @param separator
	 *            the separator to put between each string
	 * @param strings
	 *            the strings to concat
	 * @return a string with all the object represented, separated by the given
	 *         separator
	 */
	public static String concat(String separator, String... strings) {
		if (separator == null) {
			separator = "";
		}
		String result = "";
		if (strings != null) {
			for (String str : strings) {
				result += separator + (str != null ? str : "");
			}
			result = result.substring(separator.length());
		}
		return result;
	}

	/**
	 * Indent a complete text, putting a given string at the beginning of each
	 * line of the text. Null arguments are considered as empty strings ("").
	 * 
	 * @param text
	 *            the text to indent
	 * @param indent
	 *            the indent to apply
	 * @return the indented text
	 */
	public static String indentText(String text, String indent) {
		if (text == null) {
			text = "";
		}
		if (indent == null) {
			indent = "";
		}
		return indent + text.replaceAll("\n", "\n" + indent);
	}
}
