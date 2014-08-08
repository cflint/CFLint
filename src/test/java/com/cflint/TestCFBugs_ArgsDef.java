package com.cflint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.ArgDefChecker;

import cfml.parsing.CFMLParser;
import cfml.parsing.CFMLSource;
import cfml.parsing.cfscript.ParseException;

import static org.junit.Assert.*;

public class TestCFBugs_ArgsDef {

	StackHandler handler = null;
	
	@Before
	public void setUp(){
		handler = new StackHandler();
	}
	
	@Test
	public void testVarAndArgs() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\">\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new ArgDefChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_DEFAULT_MISSING",result.get(0).getMessageCode());
		assertEquals(3,result.get(0).getLine());
	}
	
	@Test
	public void testVarAndArgs_Cfscript() throws ParseException, IOException{
		final String cfcSrc="component { \r\n" + 
				"public void function foo(any arg1) { \r\n" + 
				"} \r\n" + 
				"}";
		CFLint cfBugs = new CFLint(new ArgDefChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_DEFAULT_MISSING",result.get(0).getMessageCode());
		assertEquals(2,result.get(0).getLine());
	}

	
	@Test
	public void testVarAndArgs_OK1() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"123\">\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new ArgDefChecker());
		cfBugs.process(cfcSrc,"test");
		Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0,result.size());
	}
	@Test
	public void testVarAndArgs_OK2() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" required=\"true\">\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new ArgDefChecker());
		cfBugs.process(cfcSrc,"test");
		Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0,result.size());
	}
	
	@Test
	public void testVarAndArgs_Cfscript_OK() throws ParseException, IOException{
		final String cfcSrc="component { \r\n" + 
				"public void function foo(required any arg1) { \r\n" + 
				"} \r\n" + 
				"}";
		CFLint cfBugs = new CFLint(new ArgDefChecker());
		cfBugs.process(cfcSrc,"test");
		Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0,result.size());
	}

	@Test
	public void testVarAndArgs_Cfscript_OK2() throws ParseException, IOException{
		final String cfcSrc="component { \r\n" + 
				"public void function foo(any arg1=\"123\") { \r\n" + 
				"} \r\n" + 
				"}";
		CFLint cfBugs = new CFLint(new ArgDefChecker());
		cfBugs.process(cfcSrc,"test");
		Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0,result.size());
	}


}
