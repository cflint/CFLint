package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import cfml.parsing.reporting.ParseException;

public class TestCFAbortChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final CFLintConfig conf = new CFLintConfig();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("CFXTagChecker");
		pluginRule.addParameter("tagName", "cfabort");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("AVOID_USING_CFABORT_TAG");
		pluginMessage.setSeverity("INFO");
		pluginMessage.setMessageText("Avoid Leaving <${tagName}> tags in committed code.");
		pluginRule.getMessages().add(pluginMessage);
		cfBugs = new CFLint(conf);
	}

	@Test
	public void test_BAD() throws ParseException, IOException {

		final String cfcSrc = "<cfabort>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
		assertEquals("Avoid Leaving <cfabort> tags in committed code.",
				cfBugs.getBugs().getFlatBugList().get(0).getMessage());
	}

}
