package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.cfscript.ParseException;

import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.CFTagUppercaseChecker;

public class TestCFTagUppercaseChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		cfBugs = new CFLint(conf, new CFTagUppercaseChecker());
		cfBugs.setLogError(false);
	}
	
	@Test
	public void testUpperCase_BAD() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("CFTAG_SHOULD_BE_UPPERCASE",result.get(0).getMessageCode());
	}
	
	@Test
	public void testUpperCase_GOOD() throws ParseException, IOException{
		final String cfcSrc = "<CFCOMPONENT>\r\n" +
				"<cffunction name='lowercase'>\r\n" +
				"</cffunction>\r\n" +
				"</CFCOMPONENT>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
	}
}
