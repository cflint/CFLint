package com.cflint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.ArgVarChecker;

import com.parser.main.CFMLParser;
import com.parser.main.CFMLSource;
import com.parser.main.cfscript.ParseException;

import static org.junit.Assert.*;

public class TestCFBugs_ArgsUse {

	StackHandler handler = null;
	
	@Before
	public void setUp(){
		handler = new StackHandler();
	}
	
	@Test
	public void testVarAndArgs() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"\">\r\n" +
				"	<cfset var xyz=123/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new ArgVarChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_VAR_CONFLICT",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}
	
	@Test
	public void testVarAndArgs_Cfscript() throws ParseException, IOException{
		final String cfcSrc="component { \r\n" + 
				"public void function foo(any arg1=\"\") { \r\n" + 
				"var arg1=123; \r\n" + 
				"} \r\n" + 
				"}";
		CFLint cfBugs = new CFLint(new ArgVarChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_VAR_CONFLICT",result.get(0).getMessageCode());
		assertEquals(0,result.get(0).getLine());
	}

	@Test
	public void testVarAndArgs_OK() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"\">\r\n" +
				"	<cfset xyz=123/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new ArgVarChecker());
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getBugList().size());
	}
	
	
	@Test
	public void testVarAndArgs_Struct() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"page\" default=\"\">\r\n" +
				"	<cfset variables.instance.page = arguments.page />\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new ArgVarChecker());
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getBugList().size());
	}

	
	@Test
	public void testVarAndArgs_Cfscript_OK() throws ParseException, IOException{
		final String cfcSrc="component { \r\n" + 
				"public void function foo(any arg1=\"\") { \r\n" + 
				"arg1=123; \r\n" + 
				"} \r\n" + 
				"}";
		CFLint cfBugs = new CFLint(new ArgVarChecker());
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getBugList().size());
	}
	
	@Test
	public void testVarAndArgs_MixedUse() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"\">\r\n" +
				"	<cfset xyz=123/>\r\n" +
				"	<cfset y=arguments.xyz/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new ArgVarChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_VAR_MIXED",result.get(0).getMessageCode());
		assertEquals(5,result.get(0).getLine());
	}

	@Test
	public void testVarAndArgs_MixedUse_2x() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"\">\r\n" +
				"	<cfset xyz=123/>\r\n" +
				"	<cfset y=arguments.xyz/>\r\n" +
				"	<cfset z=arguments.xyz/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new ArgVarChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_VAR_MIXED",result.get(0).getMessageCode());
		assertEquals(5,result.get(0).getLine());
	}
}
