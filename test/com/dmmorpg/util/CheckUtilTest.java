package com.dmmorpg.util;

import org.junit.Test;
import static org.junit.Assert.*;


public class CheckUtilTest {

	@Test
	public void testIsNullOrEmpty() {
		assertTrue(CheckUtil.isNullOrEmpty((String) null));
		assertTrue(CheckUtil.isNullOrEmpty(""));
		assertFalse(CheckUtil.isNullOrEmpty(" "));
		assertFalse(CheckUtil.isNullOrEmpty("a"));
		assertFalse(CheckUtil.isNullOrEmpty("test"));
	}
}
