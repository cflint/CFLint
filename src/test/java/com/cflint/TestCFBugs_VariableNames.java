package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.VariableNameChecker;

public class TestCFBugs_VariableNames {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("VariableNameChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("VAR_INVALID_NAME");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("VAR_ALLCAPS_NAME");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("VAR_TOO_SHORT");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("VAR_TOO_LONG");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("VAR_TOO_WORDY");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("VAR_IS_TEMPORARY");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("VAR_HAS_PREFIX");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new VariableNameChecker());
	}

	@Test
	public void testValidNames() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfset var firstName = \"Justin\">\r\n"
		 + "	<cfset first_name = \"Justin\">\r\n"
		 + "	<cfset firstname = \"Justin\">\r\n"
		 + "	<cfset name.first = \"Justin\">\r\n"
		 + "	<cfset name.last = \"Mclean\">\r\n"
		 + "	<cfset names[1] = \"Mclean\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testUpercaseName() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfset var FIRSTNAME = \"Justin\">\r\n"
		 + "	<cfset LAST_NAME = \"Mclean\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("VAR_ALLCAPS_NAME", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("VAR_ALLCAPS_NAME", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
	}

	@Test
	public void invalidCharsInName() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfset var $name = \"Justin\">\r\n"
		 + "	<cfset last$name = \"Mclean\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("VAR_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("VAR_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(4, result.get(0).getLine());
	}


	@Test
	public void nameEndsINNumber() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfset name_1 = \"Justin\">\r\n"
		 + "	<cfset name2 = \"Mclean\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("VAR_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("VAR_INVALID_NAME", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
	}

}
