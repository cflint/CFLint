package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.ArgTypeChecker;

import cfml.parsing.reporting.ParseException;

public class TestCFBugs_ArgsType {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final CFLintConfig conf = new CFLintConfig();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("ArgTypeChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessageMissing = new PluginMessage("ARG_TYPE_MISSING");
		pluginMessageMissing.setSeverity("WARNING");
		pluginMessageMissing.setMessageText("Argument ${variable} is missing a type.");
		pluginRule.getMessages().add(pluginMessageMissing);
		final PluginMessage pluginMessageAny = new PluginMessage("ARG_TYPE_ANY");
		pluginMessageAny.setSeverity("WARNING");
		pluginMessageAny.setMessageText("Argument ${variable} is any. Please change to be the correct type.");
		pluginRule.getMessages().add(pluginMessageAny);

		cfBugs = new CFLint(conf, new ArgTypeChecker());
	}

	@Test
	public void testMissingType() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\" returnType=\"void\">\r\n"
		 + "	<cfargument name=\"xyz\" default=\"123\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>\r\n";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARG_TYPE_MISSING", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("WARNING", result.get(0).getSeverity());
		assertEquals("Argument xyz is missing a type.", result.get(0).getMessage());
	}

	@Test
	public void testMissingTypeNoTags() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "public void function test(arg1) {\r\n"
		 + "}\r\n"
		 + "}\r\n";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARG_TYPE_MISSING", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("WARNING", result.get(0).getSeverity());
		assertEquals("Argument arg1 is missing a type.", result.get(0).getMessage());
	}

	@Test
	public void testTypeAny() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\" returnType=\"void\">\r\n"
		 + "	<cfargument name=\"xyz\" default=\"123\" type=\"any\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>\r\n";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARG_TYPE_ANY", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("WARNING", result.get(0).getSeverity());
		assertEquals("Argument xyz is any. Please change to be the correct type.", result.get(0).getMessage());
	}

	@Test
	public void testTypeAnyNoTags() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "public void function test(any arg1, numeric arg2) {\r\n"
		 + "}\r\n"
		 + "}\r\n";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARG_TYPE_ANY", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("WARNING", result.get(0).getSeverity());
		assertEquals("Argument arg1 is any. Please change to be the correct type.", result.get(0).getMessage());
	}

}
