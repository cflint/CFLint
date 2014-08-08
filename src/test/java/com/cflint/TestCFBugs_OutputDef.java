package com.cflint;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.OutputParmMissing;

import cfml.parsing.cfscript.ParseException;

import static org.junit.Assert.*;

public class TestCFBugs_OutputDef {

	StackHandler handler = null;

	@Before
	public void setUp() {
		handler = new StackHandler();
	}

//	@Test
//	public void testCFCDef() throws ParseException, IOException {
//		final String cfcSrc = "<cfcomponent >\r\n" + "<cffunction name=\"test\" output=\"false\">\r\n"
//				+ "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
//				+ "</cfcomponent>";
//		final CFBugs cfBugs = new CFBugs(new OutputParmMissing());
//		cfBugs.process(cfcSrc, "test");
//		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
//		assertEquals(1, result.size());
//		assertEquals("OUTPUT_ATTR", result.get(0).getMessageCode());
//		assertEquals(2, result.get(0).getLine());
//	}

	@Test
	public void testFunctionDef() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent output=\"false\">\r\n" + "<cffunction name=\"test\">\r\n"
				+ "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
				+ "</cfcomponent>";
		final CFLint cfBugs = new CFLint(new OutputParmMissing());
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("OUTPUT_ATTR", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void test_OK() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent output=\"false\">\r\n" + "<cffunction name=\"test\" output=\"false\">\r\n"
				+ "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
				+ "</cfcomponent>";
		final CFLint cfBugs = new CFLint(new OutputParmMissing());
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

}
