package com.dmmorpg.util;

import org.junit.Test;
import static org.junit.Assert.*;


public class FormatUtilTest {

	@Test
	public void testConcat() {
		assertEquals("", FormatUtil.concat(null));
		assertEquals("", FormatUtil.concat(""));
		assertEquals("", FormatUtil.concat(null, (String) null));
		assertEquals("", FormatUtil.concat("", (String) null));
		assertEquals("", FormatUtil.concat(null, ""));
		assertEquals("", FormatUtil.concat("", ""));
		assertEquals("", FormatUtil.concat(null, null, null));
		assertEquals("", FormatUtil.concat("", null, null));
		assertEquals("", FormatUtil.concat(null, "", null));
		assertEquals("", FormatUtil.concat("", "", null));
		assertEquals("", FormatUtil.concat(null, null, ""));
		assertEquals("", FormatUtil.concat("", null, ""));
		assertEquals("", FormatUtil.concat(null, "", ""));
		assertEquals("", FormatUtil.concat("", "", ""));
		assertEquals("aze", FormatUtil.concat(null, "aze"));
		assertEquals("aze", FormatUtil.concat("", "aze"));
		assertEquals("azesdf", FormatUtil.concat(null, "aze", "sdf"));
		assertEquals("azesdf", FormatUtil.concat("", "aze", "sdf"));
		assertEquals("a,b,c", FormatUtil.concat(",", "a", "b", "c"));
		assertEquals("a--b--c", FormatUtil.concat("--", "a", "b", "c"));
	}
	
	@Test
	public void testIndentText() {
		assertEquals("", FormatUtil.indentText(null, null));
		assertEquals("", FormatUtil.indentText("", null));
		assertEquals("", FormatUtil.indentText(null, ""));
		assertEquals("", FormatUtil.indentText("", ""));
		assertEquals("aze", FormatUtil.indentText("aze", null));
		assertEquals("aze", FormatUtil.indentText("aze", ""));
		assertEquals("-aze", FormatUtil.indentText("aze", "-"));
		assertEquals("-aze\n-sdf", FormatUtil.indentText("aze\nsdf", "-"));
		assertEquals("aze\nsdf", FormatUtil.indentText("aze\nsdf", null));
		assertEquals("!!!aze\n!!!sdf", FormatUtil.indentText("aze\nsdf", "!!!"));
	}
}
