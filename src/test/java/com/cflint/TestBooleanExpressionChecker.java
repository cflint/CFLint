package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintConfig;
import com.cflint.plugins.core.BooleanExpressionChecker;

import cfml.parsing.reporting.ParseException;

public class TestBooleanExpressionChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("BooleanExpressionChecker");
		cfBugs = new CFLint(conf);
	}

	@Test
	public void testIfBooleanExpressionInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "if (a && b == true) {\r\n"
			+ "	c = 1;\r\n"
			+ "}\r\n"
			+ "else if (a or b is false) {\r\n"
			+ "	c = 2;\r\n"
			+ "} else {\r\n"
			+ "	c = 3;\r\n"
			+ "}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(1).getMessageCode());
		assertEquals(5, result.get(1).getLine());
	}

	@Test
	public void testSetBooleanExpressionInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "x = (a && b) == true;\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void testIfBooleanExpressionInTag() throws ParseException, IOException {
		final String tagSrc = "<cfif a and b is true>\r\n"
			+ "<cfset c = 1>\r\n"
			+ "<cfelseif a or b is false>\r\n"
			+ "<cfset c = 2>\r\n"
			+ "<cfelse>\r\n"
			+ "<cfset c = 3>\r\n"
			+ "</cfif>\r\n";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
		assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(1).getMessageCode());
		assertEquals(3, result.get(1).getLine());
	}

	@Test
	public void testSetBooleanExpressionInTag() throws ParseException, IOException {
		final String tagSrc = "<cfset x = (a && b) == true>";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());

	}

	@Test
	public void testReturnBooleanExpressionInScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
			+ "public function test() {\r\n"
			+ "return (x && y) == true;\r\n"
			+ "}\r\n"
			+ "}\r\n";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
	}

	@Test
	public void testReturnBooleanExpressionInTag() throws ParseException, IOException {
		final String tagSrc = "<cffunction name=\"test\">\r\n"
			+ "<cfreturn (e and f) is true>\r\n"
			+ "</cffunction>";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

}
