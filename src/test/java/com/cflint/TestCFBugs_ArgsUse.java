package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.ArgVarChecker;

import cfml.parsing.reporting.ParseException;

public class TestCFBugs_ArgsUse {

	StackHandler handler = null;
	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		handler = new StackHandler();
		final CFLintConfig conf = new CFLintConfig();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("ArgVarChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("ARG_VAR_CONFLICT");
		pluginMessage.setSeverity(Levels.ERROR);
		pluginMessage.setMessageText("Variable ${variable} should not be declared in both var and argument scopes.");
		pluginRule.getMessages().add(pluginMessage);
		final PluginMessage pluginMessage2 = new PluginMessage("ARG_VAR_MIXED");
		pluginMessage2.setSeverity(Levels.INFO);
		pluginMessage2.setMessageText("Argument ${variable} should not be referenced in local and argument scope.");
		pluginRule.getMessages().add(pluginMessage2);
		
		cfBugs = new CFLint(conf, new ArgVarChecker());
	}
	
	@Test
	public void testVarAndArgs() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"\">\r\n" +
				"	<cfset var xyz=123/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_VAR_CONFLICT",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}
	
	@Test
	public void testVarAndArgs_Cfscript() throws ParseException, IOException{
		final String cfcSrc="component { \r\n" + 
				"public void function foo(any arg1=\"\") { \r\n" + 
				"var arg1=123; \r\n" + 
				"} \r\n" + 
				"}";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_VAR_CONFLICT",result.get(0).getMessageCode());
		assertEquals(3,result.get(0).getLine());
	}

	@Test
	public void testVarAndArgs_OK() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"\">\r\n" +
				"	<cfset xyz=123/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getBugList().size());
	}
	
	
	@Test
	public void testVarAndArgs_Struct() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"page\" default=\"\">\r\n" +
				"	<cfset variables.instance.page = arguments.page />\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(cfBugs.getBugs().getBugList().toString(),0,cfBugs.getBugs().getBugList().size());
	}

	
	@Test
	public void testVarAndArgs_Cfscript_OK() throws ParseException, IOException{
		final String cfcSrc="component { \r\n" + 
				"public void function foo(any arg1=\"\") { \r\n" + 
				"arg1=123; \r\n" + 
				"} \r\n" + 
				"}";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getBugList().size());
	}
	
	@Test
	public void testVarAndArgs_MixedUse() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"\">\r\n" +
				"	<cfset xyz=123/>\r\n" +
				"	<cfset y=arguments.xyz/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_VAR_MIXED",result.get(0).getMessageCode());
		assertEquals(5,result.get(0).getLine());
	}

	@Test
	public void testVarAndArgs_MixedUse_2x() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfargument name=\"xyz\" default=\"\">\r\n" +
				"	<cfset xyz=123/>\r\n" +
				"	<cfset y=arguments.xyz/>\r\n" +
				"	<cfset z=arguments.xyz/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("ARG_VAR_MIXED",result.get(0).getMessageCode());
		assertEquals(5,result.get(0).getLine());
	}
}
