package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import cfml.parsing.reporting.ParseException;

public class TestCFIncludeChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("CFIncludeChecker");
		cfBugs = new CFLint(conf);
	}

	@Test
	public void testCfIncludeIncomponent() throws ParseException, IOException {

		final String scriptSrc = "<cfcomponent>\r\n"
			+ "<cffunction>\r\n"
			+ "<cfinclude template=\"functions.cfm\">\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>\r\n";

		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_CFINCLUDE_TAG", result.get(0).getMessageCode());
	}

	@Test
	public void testLonelyCfInclude() throws ParseException, IOException {

		final String scriptSrc = "<cffunction>\r\n"
			+ "<cfinclude template=\"functions.cfm\">\r\n"
			+ "</cffunction>\r\n";

		cfBugs.process(scriptSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

}
