package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;

import cfml.parsing.reporting.ParseException;

public class TestCFBugsTagless {

	private CFLint cfBugs;
	
	@Before
	public void setUp() throws IOException{
		CFLintConfig conf = new CFLintConfig();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("VarScoper");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("MISSING_VAR");
		pluginMessage.setSeverity(Levels.ERROR);
		pluginMessage.setMessageText("Variable ${variable} is not declared with a var statement.");
		pluginRule.getMessages().add(pluginMessage);
		
		cfBugs = new CFLint(conf);
	}
	
	@Test
	public void testSimpleCFSET() throws ParseException, IOException{
		final String cfcSrc = "component accessors=true {\r\n" + 
				"public name function init(){\r\n" + 
				"return this;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"public void function test(){\r\n" + 
				"myvar = \"test\";\r\n" + 
				"}\r\n" + 
				"}";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(7,result.get(0).getLine());
		assertEquals("myvar",result.get(0).getVariable());
	}
	}
