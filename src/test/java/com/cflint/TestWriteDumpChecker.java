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
import com.cflint.plugins.core.FunctionXChecker;
import com.cflint.plugins.core.WriteDumpChecker;

public class TestWriteDumpChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("WriteDumpChecker");
		pluginRule.setClassName("FunctionXChecker");
		pluginRule.addParameter("functionName", "writedump");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("AVOID_USING_WRITEDUMP");
		pluginMessage.setSeverity("INFO");
		pluginMessage.setMessageText("Avoid using the ${functionName} function in production code.");
		pluginRule.getMessages().add(pluginMessage);
		
		FunctionXChecker checker = new FunctionXChecker();
		checker.setParameter("functionName", "writedump");
		cfBugs = new CFLint(conf, checker);

	}

	@Test
	public void testWriteDumpinScript() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "var a = 23;\r\n"
			+ "writeDump(a);\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_WRITEDUMP", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
	}

	@Test
	public void testWriteDumpInTag() throws ParseException, IOException {
		final String tagSrc = "<cfset a = 23>\r\n"
			+ "<cfset writeDump(a)>";
			
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_WRITEDUMP", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

}
