package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.FunctionXChecker;

import cfml.parsing.reporting.ParseException;

public class TestStructNewChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final CFLintConfig conf = new CFLintConfig();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("StructNewChecker");
		pluginRule.setClassName("FunctionXChecker");
		pluginRule.addParameter("functionName", "structnew");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("AVOID_USING_STRUCTNEW");
		pluginMessage.setSeverity(Levels.INFO);
		pluginMessage
				.setMessageText("Avoid using the ${functionName} function. Use implict structure construction instead (= {}).");
		pluginRule.getMessages().add(pluginMessage);
		FunctionXChecker checker = new FunctionXChecker();
		checker.setParameter("functionName", "structnew");
		cfBugs = new CFLint(conf, checker);
	}

	@Test
	public void testStructNewInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "var a = 23;\r\n"
			+ "var b = structNew();\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_STRUCTNEW", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
	}

	@Test
	public void testStructNewInTag() throws ParseException, IOException {
		final String tagSrc = "<cfset a = 23>\r\n"
			+ "<cfset b = structNew()>";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_STRUCTNEW", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

}
