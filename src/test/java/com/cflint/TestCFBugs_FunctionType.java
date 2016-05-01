package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.FunctionTypeChecker;

public class TestCFBugs_FunctionType {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("ArgTypeChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessageMissing = new PluginMessage("FUNCTION_TYPE_MISSING");
		pluginMessageMissing.setSeverity("WARNING");
		pluginMessageMissing.setMessageText("Function ${variable} is missing a hint.");
		pluginRule.getMessages().add(pluginMessageMissing);
		final PluginMessage pluginMessageAny = new PluginMessage("FUNCTION_TYPE_ANY");
		pluginMessageAny.setSeverity("INFO");
		pluginMessageAny.setMessageText("Function ${variable} is any, please use correct type.");
		pluginRule.getMessages().add(pluginMessageAny);

		cfBugs = new CFLint(conf, new FunctionTypeChecker());
	}

	@Test
	public void testMissingReturnType() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"xyz\" default=\"123\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>\r\n";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("FUNCTION_TYPE_MISSING", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void testMissingReturnTypeNoTags() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "public function test(arg1) {\r\n"
		 + "}\r\n"
		 + "}\r\n";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("FUNCTION_TYPE_MISSING", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void testReturnTypeAny() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\" returnType=\"any\">\r\n"
		 + "	<cfargument name=\"xyz\" default=\"123\" type=\"any\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>\r\n";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("FUNCTION_TYPE_ANY", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void testReturnTypeAnyNoTags() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "public any function test(any arg1, numeric arg2) {\r\n"
		 + "}\r\n"
		 + "}\r\n";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("FUNCTION_TYPE_ANY", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

}
