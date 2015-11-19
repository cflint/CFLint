package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.UnusedArgumentChecker;

public class TestUnusedArgumentChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("UnusedArgumentChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("UNUSED_METHOD_ARGUMENT");
		pluginMessage.setSeverity("INFO");
		cfBugs = new CFLint(conf, new UnusedArgumentChecker());
	}

	@Test
	public void testNoArgumentsInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function dummyFunction() {\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentsUsedInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function sum(a,b) {\r\n"
			+ "return a + b;\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentsNotUsedInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function sum(a,b) {\r\n"
			+ "c = a + b;"
			+ "return c;\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentsNotUsedInMethodScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function sum(a,b) {\r\n"
			+ "return other(a,b);\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentsNotUsedInReturnScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function sum(a,b) {\r\n"
			+ "return a;\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("UNUSED_METHOD_ARGUMENT", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
	}

	@Test
	public void testMultipleArgumentsNotUsedScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function sum(a,b,c,d) {\r\n"
			+ "return a;\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(3, result.size());
		assertEquals("UNUSED_METHOD_ARGUMENT", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("UNUSED_METHOD_ARGUMENT", result.get(1).getMessageCode());
		assertEquals(3, result.get(1).getLine());
		assertEquals("UNUSED_METHOD_ARGUMENT", result.get(2).getMessageCode());
		assertEquals(3, result.get(2).getLine());
	}

	@Test
	public void testNoArgumentsInTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"dummyFunction\">\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>\r\n";
			
		cfBugs.process(tagSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentsUsedInTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"sum\">\r\n"
			+ "<cfargument name=\"a\">\r\n"
			+ "<cfargument name=\"b\">\r\n"
			+ "<cfreturn a + b>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>\r\n";
			
		cfBugs.process(tagSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentsNotUsedInTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"sum\">\r\n"
			+ "<cfargument name=\"a\">\r\n"
			+ "<cfargument name=\"b\">\r\n"
			+ "<cfset c = a + b>"
			+ "<cfreturn c>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>\r\n";
			
		cfBugs.process(tagSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentsNotUsedInMethodTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"sum\">\r\n"
			+ "<cfargument name=\"a\">\r\n"
			+ "<cfargument name=\"b\">\r\n"
			+ "<cfreturn other(a,b)>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>\r\n";
			
		cfBugs.process(tagSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentsNotUsedInReturnTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"sum\">\r\n"
			+ "<cfargument name=\"a\">\r\n"
			+ "<cfargument name=\"b\">\r\n"
			+ "<cfreturn a>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>\r\n";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("UNUSED_METHOD_ARGUMENT", result.get(0).getMessageCode());
		assertEquals(4, result.get(0).getLine());
	}

	@Test
	public void testMultipleArgumentsNotUsedTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"sum\">\r\n"
			+ "<cfargument name=\"a\">\r\n"
			+ "<cfargument name=\"b\">\r\n"
			+ "<cfargument name=\"c\">\r\n"
			+ "<cfargument name=\"d\">\r\n"
			+ "<cfreturn a>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>\r\n";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(3, result.size());
		assertEquals("UNUSED_METHOD_ARGUMENT", result.get(0).getMessageCode());
		assertEquals(6, result.get(0).getLine());
		assertEquals("UNUSED_METHOD_ARGUMENT", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
		assertEquals("UNUSED_METHOD_ARGUMENT", result.get(2).getMessageCode());
		assertEquals(5, result.get(2).getLine());		
	}

}
