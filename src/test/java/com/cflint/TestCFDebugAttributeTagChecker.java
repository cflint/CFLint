package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.CFDebugAttributeChecker;
import com.cflint.plugins.core.CFXTagChecker;

public class TestCFDebugAttributeTagChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRuleX = new PluginInfoRule();
		pluginRuleX.setName("CFDebugAttributeChecker");
		conf.getRules().add(pluginRuleX);
		final PluginMessage pluginMessageX = new PluginMessage("AVOID_USING_DEBUG_ATTR");
		pluginMessageX.setSeverity("WARNING");
		pluginMessageX
				.setMessageText("Avoid leaving debug attribute on tags.");
		pluginRuleX.getMessages().add(pluginMessageX);
		final PluginMessage pluginMessage2 = new PluginMessage("AVOID_USING_CFSETTING_DEBUG");
		pluginMessage2.setSeverity("WARNING");
		pluginMessage2
				.setMessageText("Avoid using <cfsetting showDebugOutput=\"Yes\">");
		pluginRuleX.getMessages().add(pluginMessageX);
		final CFDebugAttributeChecker checker = new CFDebugAttributeChecker();
		cfBugs = new CFLint(conf, checker);
	}

	@Test
	public void test_debugNoVal() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" debug> \n" + 
				"    SELECT * FROM TestTable \n" + 
				"</cfquery>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_DEBUG_ATTR").size());
		System.out.println(cfBugs.getBugs().getBugList().get("AVOID_USING_DEBUG_ATTR"));
	}

	@Test
	public void test_debugNoValCASE() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" DEBUG> \n" + 
				"    SELECT * FROM TestTable \n" + 
				"</cfquery>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_DEBUG_ATTR").size());
	}

	@Test
	public void test_debugWithVal() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" debug=false> \n" + 
				"    SELECT * FROM TestTable \n" + 
				"</cfquery>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_DEBUG_ATTR").size());
	}
	

	@Test
	public void test_cfsetting_debug_no() throws ParseException, IOException {
		final String cfcSrc = "<cfsetting showDebugOutput=\"No\">";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}
	@Test
	public void test_cfsetting_debug_yes() throws ParseException, IOException {
		final String cfcSrc = "<cfsetting showDebugOutput=\"Yes\">";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_CFSETTING_DEBUG").size());
	}
}
