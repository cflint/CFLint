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

public class TestDebugChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final CFLintConfig conf = new CFLintConfig();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("DebugChecker");
		pluginRule.setClassName("FunctionXChecker");
		pluginRule.addParameter("functionName", "IsDebugMode");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("AVOID_USING_ISDEBUGMODE");
		pluginMessage.setSeverity(Levels.WARNING);
		pluginMessage.setMessageText("Avoid using the ${functionName} function in production code.");
		pluginRule.getMessages().add(pluginMessage);
		
		FunctionXChecker checker = new FunctionXChecker();
		checker.setParameter("functionName", "IsDebugMode");
		cfBugs = new CFLint(conf, checker);

	}

	@Test
	public void testIsDebugModeInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfif IsDebugMode()> \n" + 
				"    <cflog file=\"MyAppSilentTrace\" text=\"Page: #cgi.script_name#, \n" + 
				"    completed query MyDBQuery; Query Execution time: \n" + 
				"    #cfquery.ExecutionTime# Status: #Application.status#\"> \n" + 
				"</cfif>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_ISDEBUGMODE", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}


	@Test
	public void testIsDebugModeUpperCaseInScript() throws ParseException, IOException {
		final String scriptSrc = "<cfif ISDEBUGMODE()> \n" + 
				"    <cflog file=\"MyAppSilentTrace\" text=\"Page: #cgi.script_name#, \n" + 
				"    completed query MyDBQuery; Query Execution time: \n" + 
				"    #cfquery.ExecutionTime# Status: #Application.status#\"> \n" + 
				"</cfif>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_ISDEBUGMODE", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}
}
