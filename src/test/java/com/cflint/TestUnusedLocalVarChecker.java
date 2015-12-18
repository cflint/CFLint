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
import com.cflint.plugins.core.UnusedLocalVarChecker;

public class TestUnusedLocalVarChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("UnusedLocalVarChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("UNUSED_LOCAL_VARIABLE");
		pluginMessage.setSeverity("INFO");
		cfBugs = new CFLint(conf, new UnusedLocalVarChecker());
	}

	@Test
	public void testAllUsedInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function someFunction() {\r\n"
			+ "var a = 1;\r\n"
			+ "var b = 2;\r\n"
			+ "var c = 3;\r\n"
			+ "return c * (a + b);\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentFunctionInScript() throws ParseException, IOException {
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
	public void testUnusedVarInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function someFunction() {\r\n"
			+ "var a = 1;\r\n"
			+ "var b = 2;\r\n"
			+ "var c = 2;\r\n"
			+ "var sum = a + b;\r\n"
			+ "return sum;\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
		assertEquals(6, result.get(0).getLine());
	}

	@Test
	public void testUnusedVarReturnInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function someFunction() {\r\n"
			+ "var a = 1;\r\n"
			+ "var b = 2;\r\n"
			+ "var c = 2;\r\n"
			+ "return a + b;\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
		assertEquals(6, result.get(0).getLine());
	}

	@Test
	public void testUnusedVarFunctionInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function someFunction() {\r\n"
			+ "var a = 1;\r\n"
			+ "var b = 2;\r\n"
			+ "var c = 2;\r\n"
			+ "return otherFunc(a,b);\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
		assertEquals(6, result.get(0).getLine());
	}

	@Test
	public void testMultipleUnusedVarFunctionInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "component {\r\n"
			+ "function someFunction() {\r\n"
			+ "var a = 1;\r\n"
			+ "var b = 2;\r\n"
			+ "var c = 2;\r\n"
			+ "var d = 2;\r\n"
			+ "var e = 2;\r\n"
			+ "return otherFunc(a,b);\r\n"
			+ "}\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(3, result.size());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
		assertEquals(6, result.get(0).getLine());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(1).getMessageCode());
		assertEquals(7, result.get(1).getLine());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(2).getMessageCode());
		assertEquals(8, result.get(2).getLine());
	}

	public void testAllUsedInTags() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction someFunction() {\r\n"
			+ "<cfset var a = 1>\r\n"
			+ "<cfset var b = 2>\r\n"
			+ "<cfset var c = 3>\r\n"
			+ "<cfreturn c * (a + b)>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>";
			
		cfBugs.process(tagSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testArgumentFunctionInTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"sum\">\r\n"
			+ "<cfreturn a + b>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>";
			
		cfBugs.process(tagSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testUnusedVarInTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"someFunction\">\r\n"
			+ "<cfset var a = 1>\r\n"
			+ "<cfset var b = 2>\r\n"
			+ "<cfset var c = 2>\r\n"
			+ "<cfset var sum = a + b>\r\n"
			+ "<cfreturn sum>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
		assertEquals(5, result.get(0).getLine());
	}

	@Test
	public void testUnusedVarReturnInTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"someFunction\">\r\n"
			+ "<cfset var a = 1>\r\n"
			+ "<cfset var b = 2>\r\n"
			+ "<cfset var c = 2>\r\n"
			+ "<cfreturn a + b>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>";

		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
		assertEquals(5, result.get(0).getLine());
	}

	@Test
	public void testUnusedVarFunctionInTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"someFunction\">\r\n"
			+ "<cfset var a = 1>\r\n"
			+ "<cfset var b = 2>\r\n"
			+ "<cfset var c = 2>\r\n"
			+ "<cfreturn otherFunc(a,b)>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
		assertEquals(5, result.get(0).getLine());
	}

	@Test
	public void testMultipleUnusedVarFunctionInTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
			+ "<cffunction name=\"someFunction\">\r\n"
			+ "<cfset var a = 1>\r\n"
			+ "<cfset var b = 2>\r\n"
			+ "<cfset var c = 2>\r\n"
			+ "<cfset var d = 3>\r\n"
			+ "<cfset var e = 4>\r\n"
			+ "<cfreturn otherFunc(a,b)>\r\n"
			+ "</cffunction>\r\n"
			+ "</cfcomponent>";

		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().get("UNUSED_LOCAL_VARIABLE");
		assertEquals(3, result.size());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
		assertEquals(5, result.get(0).getLine());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(1).getMessageCode());
		assertEquals(6, result.get(1).getLine());
		assertEquals("UNUSED_LOCAL_VARIABLE", result.get(2).getMessageCode());
		assertEquals(7, result.get(2).getLine());
	}

}
