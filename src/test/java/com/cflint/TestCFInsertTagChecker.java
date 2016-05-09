package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.CFXTagChecker;

import cfml.parsing.reporting.ParseException;

public class TestCFInsertTagChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRuleX = new PluginInfoRule();
		pluginRuleX.setName("CFXTagChecker");
		pluginRuleX.addParameter("tagName", "cfinsert");
		conf.getRules().add(pluginRuleX);
		final PluginMessage pluginMessageX = new PluginMessage("AVOID_USING_CFINSERT_TAG");
		pluginMessageX.setSeverity("WARNING");
		pluginMessageX
				.setMessageText("Avoid Leaving <${tagName}> tags in committed code. Debug information should be ommited from release code");
		pluginRuleX.getMessages().add(pluginMessageX);
		final CFXTagChecker checker = new CFXTagChecker();
		checker.setParameter("tagName", "cfinsert");
		cfBugs = new CFLint(conf, checker);
	}

	@Test
	public void test_BAD() throws ParseException, IOException {
		final String cfcSrc = "<cfinsert " + "dataSource = \"data source name\" " + "tableName = \"table name\" "
				+ "formFields = \"formfield1, formfield2, ...\" " + "password = \"password\" "
				+ "tableOwner = \"owner\" " + "tableQualifier = \"table qualifier\" " + "username = \"user name\">";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

	@Test
	public void test_GOOD() throws ParseException, IOException {
		final String cfcSrc = "<cfmodule template=\"tagsExchRateCalculator.cfm\">";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

}
