package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.SimpleComplexityChecker;

public class TestCFBugs_SimpleComplexity {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("SimpleComplexityChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("FUNCTION_TOO_COMPLEX");
		pluginMessage.setSeverity("WARNING");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new SimpleComplexityChecker());
	}

	@Test
	public void testNotComplexTagBased() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"functionOne\">\r\n"
		 + "<cfif a is 1>\r\n"
		 + "<cfset b = 1>\r\n"
		 + "<cfelse>\r\n"
		 + "<cfset b = 2>\r\n"
		 + "</cfif>\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testComplexTagBased() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"functionTwo\">\r\n"
		 + "<cfif a is 1>\r\n"
		 + "<cfset b = 1>\r\n"
		 + "<cfelseif a is 2>\r\n"
		 + "<cfset b = 2>\r\n"
		 + "<cfelseif a is 3>\r\n"
		 + "<cfset b = 3>\r\n"
		 + "<cfelseif a is 4>\r\n"
		 + "<cfset b = 4>\r\n"
		 + "<cfelseif a is 5>\r\n"
		 + "<cfset b = 5>\r\n"
		 + "<cfelseif a is 6>\r\n"
		 + "<cfset b = 6>\r\n"
		 + "<cfelseif a is 7>\r\n"
		 + "<cfset b = 7>\r\n"
		 + "<cfelseif a is 8>\r\n"
		 + "<cfset b = 8>\r\n"
		 + "<cfelseif a is 9>\r\n"
		 + "<cfset b = 9>\r\n"
		 + "<cfelseif a is 10>\r\n"
		 + "<cfset b = 10>\r\n"
		 + "<cfelseif a is 11>\r\n"
		 + "<cfset b = 11>\r\n"	 		 		 		 		 		 		 
		 + "</cfif>\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("FUNCTION_TOO_COMPLEX", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void testNonComplexNameScriptBased() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "function functionThree() {\r\n"
		 + "if (a == 1) {"
		 + "b = 1;\r\n"
		 + "}\r\n"
		 + "else {\r\n"
		 + "b = 2;\r\n"	 		 		 		 		 		 		 
		 + "}\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(cfcSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testComplexScriptBased() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "function functionFour() {\r\n"
		 + "if (a == 1) {"
		 + "b = 1;\r\n"
		 + "}\r\n"
		 + "else if (a == 2) {\r\n"
		 + "b = 2;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"
		 + "else if (a == 3) {\r\n"
		 + "b = 3;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"
		 + "else if (a == 4) {\r\n"
		 + "b = 4;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"
		 + "else if (a == 5) {\r\n"
		 + "b = 5;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"
		 + "else if (a == 6) {\r\n"
		 + "b = 6;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"
		 + "else if (a == 7) {\r\n"
		 + "b = 7;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"	
		 + "else if (a == 8) {\r\n"
		 + "b = 8;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"
		 + "else if (a == 9) {\r\n"
		 + "b = 9;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"
		 + "else if (a == 10) {\r\n"
		 + "b = 10;\r\n" 
		 + "else if (a == 11) {\r\n"
		 + "b = 11;\r\n" 		 		 		 		 		 		 
		 + "}\r\n"	
		 + "}\r\n" 		 		 		 
		 + "}";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("FUNCTION_TOO_COMPLEX", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void testComplexSwitchScriptBased() throws ParseException, IOException {
		final String cfcSrc = "component {\r\n"
		 + "function functionFive() {\r\n"
		 + "switch(a) {\r\n"
		 + "case 1:\r\n"
		 + "b = 1;\r\n"
		 + "break;\r\n"
		 + "case 2:\r\n"
		 + "b = 2;\r\n"
		 + "break;\r\n"
		 + "case 3:\r\n"
		 + "b = 3;\r\n"
		 + "break;\r\n"
		 + "case 4:\r\n"
		 + "b = 4;\r\n"
		 + "break;\r\n"
		 + "case 5:\r\n"
		 + "b = 5;\r\n"
		 + "break;\r\n"
		 + "case 6:\r\n"
		 + "b = 6;\r\n"
		 + "break;\r\n"
		 + "case 7:\r\n"
		 + "b = 7;\r\n"
		 + "break;\r\n"
		 + "case 8:\r\n"
		 + "b = 8;\r\n"
		 + "break;\r\n"
		 + "case 9:\r\n"
		 + "b = 9;\r\n"
		 + "break;\r\n"
		 + "case 10:\r\n"
		 + "b = 10;\r\n"
		 + "break;\r\n"
		 + "case 11:\r\n"
		 + "b = 11;\r\n"
		 + "break;\r\n"		 	 		 		 		 		 		 
		 + "}\r\n"	
		 + "}\r\n" 		 		 		 
		 + "}";
		cfBugs.process(cfcSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
		// TODO currently this is seen as simple code change test once fixed
		//assertEquals("FUNCTION_TOO_COMPLEX", result.get(0).getMessageCode());
		//assertEquals(1, result.get(0).getLine());
	}


}
