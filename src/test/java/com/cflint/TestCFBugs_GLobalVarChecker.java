package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.cfscript.ParseException;

import com.cflint.plugins.core.GlobalVarChecker;

public class TestCFBugs_GLobalVarChecker {

	StackHandler handler = null;
	
	@Before
	public void setUp(){
		handler = new StackHandler();
	}
	
	@Test
	public void testApplication() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfset application.bf=\"123\">\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new GlobalVarChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("GLOBAL_VAR",result.get(0).getMessageCode());
		assertEquals("application",result.get(0).getVariable());
		assertEquals(3,result.get(0).getLine());
	}

	@Test
	public void testApplication_1x() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfset application.bf=\"123\">\r\n" +
				"	<cfset application.another=\"123\">\r\n" +
				"	<cfset form.id=\"123\">\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new GlobalVarChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2,result.size());
		assertEquals("GLOBAL_VAR",result.get(0).getMessageCode());
		assertEquals("application",result.get(0).getVariable());
		assertEquals(3,result.get(0).getLine());
		assertEquals("GLOBAL_VAR",result.get(1).getMessageCode());
		assertEquals("form",result.get(1).getVariable());
		assertEquals(5,result.get(1).getLine());
	}


}
