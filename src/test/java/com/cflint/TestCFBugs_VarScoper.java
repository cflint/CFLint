package com.cflint;
/**
 * tests from 
 * https://github.com/mschierberl/varscoper/blob/master/varScoper.cfc
 * 
 */
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.VarScoper;

import cfml.parsing.reporting.ParseException;

public class TestCFBugs_VarScoper {

	private CFLint cfBugs;
	
	@Before
	public void setUp(){
		ConfigRuntime conf = new ConfigRuntime();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("VarScoper");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("MISSING_VAR");
		pluginMessage.setSeverity("ERROR");
		pluginMessage.setMessageText("Variable ${variable} is not declared with a var statement.");
		pluginRule.getMessages().add(pluginMessage);
		
		cfBugs = new CFLint(conf,new VarScoper());
	}
	
	@Test
	public void testCFProcParam_Out() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"   <cfset var yy=\"\"/>\r\n" +
				"	<cfstoredproc name=\"yy\" >\r\n" +
				"	   <cfprocparam variable=\"xx\" type=\"out\">\r\n" +
				"	</cfstoredproc>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(1,cfBugs.getBugs().getBugList().size());
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals("xx",result.get(0).getVariable());
		assertEquals(5,result.get(0).getLine());
	}
	
	@Test
	public void testCFProcParam_InOut() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"   <cfset var yy=\"\"/>\r\n" +
				"	<cfstoredproc name=\"yy\" >\r\n" +
				"	   <cfprocparam variable=\"xx\" type=\"inout\">\r\n" +
				"	</cfstoredproc>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(1,cfBugs.getBugs().getBugList().size());
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals("xx",result.get(0).getVariable());
		assertEquals(5,result.get(0).getLine());
	}
	
	@Test
	public void testCFProcParam_In() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"   <cfset var yy=\"\"/>\r\n" +
				"	<cfstoredproc name=\"yy\" >\r\n" +
				"	   <cfprocparam variable=\"xx\" type=\"in\">\r\n" +
				"	</cfstoredproc>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getBugList().size());
	}	
	
	@Test
	public void testCFFeed_Read_Vard() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"   <cfset var yy=\"\"/>\r\n" +
				"	<cffeed query=\"yy\" action=\"read\">\r\n" +
				"	</cffeed>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getBugList().size());
	}
	
	@Test
	public void testCFFeed_Read_UnVard() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cffeed query=\"yy\" action=\"read\">\r\n" +
				"	</cffeed>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(1,cfBugs.getBugs().getBugList().size());
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals("yy",result.get(0).getVariable());
		assertEquals(3,result.get(0).getLine());
	}
	
	@Test
	public void testScript_UnVard() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfscript>\r\n" +
				"   yy = 123;\r\n" +
				"	</cfscript>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(1,cfBugs.getBugs().getFlatBugList().size());
		List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals("yy",result.get(0).getVariable());
		assertEquals(4,result.get(0).getLine());
		assertEquals("ERROR",result.get(0).getSeverity());
		assertEquals("Variable yy is not declared with a var statement.",result.get(0).getMessage());
	}
	
	
	@Test
	public void testScript_UnVard_Scoped() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfscript>\r\n" +
				"   cfevent.yy = 123;\r\n" +
				"	</cfscript>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getFlatBugList().size());
	}
	

	
	@Test
	public void testScript_UnVard_ArrayFalsePositive() throws ParseException, IOException{
		final String cfcSrc = "<cffunction name=\"x\"><cfset var myRet = [] />\n" + 
				"\n" + 
				"<cfstoredproc>\n" + 
				"   <cfprocresult name=\"myRet[1]\" resultset=\"1\" />\n" + 
				"   <cfprocresult name=\"myRet[2]\" resultset=\"2\" />\n" + 
				"   <cfprocresult name=\"myRet[3]\" resultset=\"3\" />\n" + 
				"</cfstoredproc></cffunction>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().getFlatBugList().size());
	}
	
	
	
}
