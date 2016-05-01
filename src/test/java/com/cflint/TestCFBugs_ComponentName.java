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
import com.cflint.plugins.core.ComponentDisplayNameChecker;

public class TestCFBugs_ComponentName {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("ComponentDisplayNameChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("USE_DISPLAY_NAME");
		pluginMessage.setSeverity("WARNING");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new ComponentDisplayNameChecker());
	}

	@Test
	public void testDisplayNameTagBased() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent displayName=\"test\">\r\n</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testNameTagBased() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent name=\"test\">\r\n</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("USE_DISPLAY_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	// TODO
	public void testDisplayNameScriptBased() throws ParseException, IOException {
		final String cfcSrc = "component displayName=\"test\" {}";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(0, result.size());
	}

	// TODO
	public void testNameScriptBased() throws ParseException, IOException {
		final String cfcSrc = "component name=\"test\" {}";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("USE_DISPLAY_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

}
