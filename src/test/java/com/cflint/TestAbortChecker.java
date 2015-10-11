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
import com.cflint.plugins.core.AbortChecker;

public class TestAbortChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("AbortChecker");
		pluginRule.addParameter("tagName", "cfabort");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("AVOID_USING_ABORT");
		pluginMessage.setSeverity("WARNING");
		pluginRule.getMessages().add(pluginMessage);
		cfBugs = new CFLint(conf, new AbortChecker());
	}

	@Test
	public void test_no_abort() throws ParseException, IOException {
		final String cfcSrc = "<cfscript>\r\n"
		+ "a = 23;\r\n"
		+ "</cfscript>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

	@Test
	public void test_abort() throws ParseException, IOException {
		final String cfcSrc = "<cfscript>\r\n"
		+ "abort;\r\n"
		+ "</cfscript>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_ABORT", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

}
