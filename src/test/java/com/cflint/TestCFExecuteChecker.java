package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.CFXTagChecker;

import cfml.parsing.reporting.ParseException;

public class TestCFExecuteChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final CFLintConfig conf = new CFLintConfig();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("CFXTagChecker");
		pluginRule.addParameter("tagName", "cfexecute");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("AVOID_USING_CFEXECUTE_TAG");
		pluginMessage.setSeverity("INFO");
		pluginMessage.setMessageText("Avoid Leaving <${tagName}> tags in committed code.");
		pluginRule.getMessages().add(pluginMessage);
		final CFXTagChecker checker = new CFXTagChecker();
		checker.setParameter("tagName", "cfexecute");
		cfBugs = new CFLint(conf, checker);
	}

	@Test
	public void test_BAD() throws ParseException, IOException {

		final String cfcSrc = "<cfexecute name=\"date\" timeout=\"1\" variable=\"date\" />";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
		assertEquals("Avoid Leaving <cfexecute> tags in committed code.",
				cfBugs.getBugs().getFlatBugList().get(0).getMessage());
	}

}
