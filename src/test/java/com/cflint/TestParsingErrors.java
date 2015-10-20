package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.ConfigRuntime;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.VarScoper;

public class TestParsingErrors {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		ConfigRuntime conf = new ConfigRuntime();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("VarScoper");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("MISSING_VAR");
		pluginMessage.setSeverity("ERROR");
		pluginMessage
				.setMessageText("Variable ${variable} is not declared with a var statement.");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new VarScoper());
	}

	@Test
	public void testMissingSemiColon() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfscript>\r\n" +
				"   var xx = 123\r\n" +
				"   yy = 123;\r\n" +
				"	</cfscript>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		
		assertEquals(2,cfBugs.getBugs().getFlatBugList().size());
		System.out.println(cfBugs.getBugs().getFlatBugList());
		final List<BugInfo> result = cfBugs.getBugs().getBugList().get("MISSING_SEMI");
		assertEquals(1,result.size());
		assertEquals("MISSING_SEMI",result.get(0).getMessageCode());
		assertEquals(5,result.get(0).getLine());
		assertEquals("ERROR",result.get(0).getSeverity());
		assertEquals("End of statement(;) expected instead of yy",result.get(0).getMessage());
		
		final List<BugInfo> result2 = cfBugs.getBugs().getBugList().get("MISSING_VAR");
		assertEquals(1,result2.size());
		assertEquals("MISSING_VAR",result2.get(0).getMessageCode());
		assertEquals("yy",result2.get(0).getVariable());
		assertEquals(5,result2.get(0).getLine());
		assertEquals("ERROR",result2.get(0).getSeverity());
		assertEquals("Variable yy is not declared with a var statement.",result2.get(0).getMessage());
	}
}
