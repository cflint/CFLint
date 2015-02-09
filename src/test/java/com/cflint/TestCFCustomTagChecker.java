package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.cfscript.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.CFXTagChecker;

public class TestCFCustomTagChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRuleX = new PluginInfoRule();
		pluginRuleX.setName("CFXTagChecker");
		pluginRuleX.addParameter("tagName", "cfcustomtag");
		conf.getRules().add(pluginRuleX);
		final PluginMessage pluginMessageX = new PluginMessage("AVOID_USING_CFUPDATE_TAG");
		pluginMessageX.setSeverity("WARNING");
		pluginMessageX
				.setMessageText("Avoid Leaving <${tagName}> tags in committed code. Debug information should be ommited from release code");
		pluginRuleX.getMessages().add(pluginMessageX);
		final CFXTagChecker checker = new CFXTagChecker();
		checker.setParameter("tagName", ".*:cfcustomtag");
		cfBugs = new CFLint(conf, checker);
	}

	@Test
	public void test_BAD() throws ParseException, IOException {
		final String cfcSrc = "<mylib:cfcustomtag x=1/>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

	@Test
	public void test_GOOD() throws ParseException, IOException {
		final String cfcSrc = "<customtag>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}
}
