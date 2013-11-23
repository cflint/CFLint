package com.cfbugs;

import org.junit.Test;

import com.cfbugs.BugInfo;
import com.cfbugs.tools.CFBugsFilter;

import static org.junit.Assert.*;

public class TestCFBugsFilter {

	@Test
	public void testExclude1() {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").build();
		CFBugsFilter filter = CFBugsFilter.createFilter("[{\"code\":\"PARSE_ERROR\"}]");
		assertFalse(filter.include(bugInfo));
	}
	@Test
	public void testExclude2_Regex() {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").build();
		CFBugsFilter filter = CFBugsFilter.createFilter("[{\"code\":\"PARSE_ER.*\"}]");
		assertFalse(filter.include(bugInfo));
	}
	@Test
	public void testInclude1() {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").build();
		CFBugsFilter filter = CFBugsFilter.createFilter("[{\"code\":\"OTH_ERROR\"}]");
		assertTrue(filter.include(bugInfo));
	}
}
