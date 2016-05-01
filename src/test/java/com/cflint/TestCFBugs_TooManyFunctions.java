package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.TooManyFunctionsChecker;

public class TestCFBugs_TooManyFunctions {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("TooManyFunctionsChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("EXCESSIVE_FUNCTIONS");
		pluginMessage.setSeverity("WARNING");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new TooManyFunctionsChecker());
	}

	@Test
	public void testSmallNumberTagBased() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"functionOne\" />\r\n"
		 + "<cffunction name=\"functionTwo\" />\r\n"
		 + "<cffunction name=\"functionThree\" />\r\n"
		 + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testSmallNumberScriptBased() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "public void function functionOne() {}\r\n"
		 + "public void function functionTwo() {}\r\n"
		 + "public void function functionThree() {}\r\n"
		 + "}";
		cfBugs.process(cfcSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testLargeNumberTagBased() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"functionOne\" />\r\n"
		 + "<cffunction name=\"functionTwo\" />\r\n"
		 + "<cffunction name=\"functionThree\" />\r\n"
		 + "<cffunction name=\"functionFour\" />\r\n"
		 + "<cffunction name=\"functionFive\" />\r\n"
		 + "<cffunction name=\"functionSix\" />\r\n"
		 + "<cffunction name=\"functionSeven\" />\r\n"
		 + "<cffunction name=\"functionEight\" />\r\n"
		 + "<cffunction name=\"functionNine\" />\r\n"
		 + "<cffunction name=\"functionTen\" />\r\n"
		 + "<cffunction name=\"functionEleven\" />\r\n"		 
		 + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("EXCESSIVE_FUNCTIONS", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void testLargeNumberScriptBased() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "public void function functionOne() {}\r\n"
		 + "public void function functionTwo() {}\r\n"
		 + "public void function functionThree() {}\r\n"
		 + "public void function functionFour() {}\r\n"
		 + "public void function functionFive() {}\r\n"
		 + "public void function functionSix() {}\r\n"
		 + "public void function functionSeven() {}\r\n"
		 + "public void function functionEight() {}\r\n"
		 + "public void function functionNine() {}\r\n"
		 + "public void function functionTen() {}\r\n"
		 + "public void function functionEleven() {}\r\n"
		 + "}";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("EXCESSIVE_FUNCTIONS", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

}
