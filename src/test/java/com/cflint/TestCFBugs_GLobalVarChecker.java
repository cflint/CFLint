package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.GlobalVarChecker;

import cfml.parsing.reporting.ParseException;

public class TestCFBugs_GLobalVarChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("GlobalVarChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("GLOBAL_VAR");
		pluginMessage.setSeverity("WARNING");
		pluginMessage
				.setMessageText("Identifier ${variable} is global, referencing in a CFC or function should be avoided.");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new GlobalVarChecker());
	}

	@Test
	public void testApplication() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
				+ "	<cfset application.bf=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
		assertEquals("application", result.get(0).getVariable());
		assertEquals(3, result.get(0).getLine());
	}

	@Test
	public void testApplication_1x() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
				+ "	<cfset application.bf=\"123\">\r\n" + "	<cfset application.another=\"123\">\r\n"
				+ "	<cfset form.id=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
		assertEquals("application", result.get(0).getVariable());
		assertEquals(3, result.get(0).getLine());
		assertEquals("GLOBAL_VAR", result.get(1).getMessageCode());
		assertEquals("form", result.get(1).getVariable());
		assertEquals(5, result.get(1).getLine());
	}
	
	@Test
	public void testCGI() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
				+ "	<cfset CGI.bf=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
		assertEquals("CGI", result.get(0).getVariable());
		assertEquals(3, result.get(0).getLine());
	}
	
	@Test
	public void testHashNestedCGI() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
				+ "	<cfset a.books = #CGI.user.books#>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
		assertEquals("CGI", result.get(0).getVariable());
		assertEquals(3, result.get(0).getLine());
	}
	
	@Test
	public void testHashLiteralNestedCGI() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
				+ "	<cfset a.books = '#CGI.user.books#'>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
		assertEquals("CGI", result.get(0).getVariable());
		assertEquals(3, result.get(0).getLine());
	}
	

}
