package com.cflint;

import org.junit.Test;

import com.cflint.BugInfo;
import com.cflint.tools.CFLintFilter;

import static org.junit.Assert.*;

public class TestCFBugsFilter {

	@Test
	public void testExclude1() {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").build();
		CFLintFilter filter = CFLintFilter.createFilter("[{\"code\":\"PARSE_ERROR\"}]");
		assertFalse(filter.include(bugInfo));
	}
	@Test
	public void testExclude2_Regex() {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").build();
		CFLintFilter filter = CFLintFilter.createFilter("[{\"code\":\"PARSE_ER.*\"}]");
		assertFalse(filter.include(bugInfo));
	}
	@Test
	public void testInclude1() {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").build();
		CFLintFilter filter = CFLintFilter.createFilter("[{\"code\":\"OTH_ERROR\"}]");
		assertTrue(filter.include(bugInfo));
	}
}
