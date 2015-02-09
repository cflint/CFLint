package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.cfscript.ParseException;

import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.VarScoper;

public class TestCFBugs_ParseError {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		cfBugs = new CFLint(conf, new VarScoper());
		cfBugs.setLogError(true);
	}
	
	@Test
	public void testSimpleCFSET() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfset var x={}/>\r\n" +
				"	<cfset var x.y=123/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("PARSE_ERROR",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}
	
	@Test
	public void testLookBackError() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfset var user = userservice[\"getuser\"]();/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		System.out.println(result.get(0));
		assertEquals(1,result.size());
		assertEquals("PARSE_ERROR",result.get(0).getMessageCode());
		assertEquals(3,result.get(0).getLine());
	}
}
