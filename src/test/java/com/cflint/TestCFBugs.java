package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;

import cfml.parsing.reporting.ParseException;

public class TestCFBugs {

	private CFLint cfBugs;
	
	@Before
	public void setUp() throws IOException{
		CFLintConfig conf = new CFLintConfig();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("VarScoper");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("MISSING_VAR");
		pluginMessage.setSeverity("ERROR");
		pluginMessage.setMessageText("Variable ${variable} is not declared with a var statement.");
		pluginRule.getMessages().add(pluginMessage);
		
		pluginRule = new PluginInfoRule();
		pluginRule.setName("GlobalVarChecker");
		conf.getRules().add(pluginRule);
		pluginMessage = new PluginMessage("GLOBAL_VAR");
		pluginMessage.setSeverity("WARNING");
		pluginMessage
				.setMessageText("Identifier ${variable} is global, referencing in a CFC or function should be avoided.");
		pluginRule.getMessages().add(pluginMessage);

		pluginRule = new PluginInfoRule();
		pluginRule.setName("NestedCFOutput");
		conf.getRules().add(pluginRule);
		pluginMessage = new PluginMessage("NESTED_CFOUTPUT");
		pluginMessage.setSeverity("ERROR");
		pluginMessage
				.setMessageText("Nested CFOutput, outer CFOutput has @query.");
		pluginRule.getMessages().add(pluginMessage);
		
		pluginRule = new PluginInfoRule();
		pluginRule.setName("TypedQueryNew");
		conf.getRules().add(pluginRule);
		pluginMessage = new PluginMessage("QUERYNEW_DATATYPE");
		pluginMessage.setSeverity("WARNING");
		pluginMessage
				.setMessageText("QueryNew statement should specify datatypes.");
		pluginRule.getMessages().add(pluginMessage);
		
		cfBugs = new CFLint(conf);
		cfBugs.setLogError(true);
	}
	
	@Test
	public void testSimpleCFSET() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfif 1 EQ 1>\r\n" +
				"	<cfset x=123/>\r\n" +
				"	<cfset var y=123/>\r\n" +
				"	</cfif>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}
	
	@Test
	public void testSimpleCFSETFirstOffenseOnly() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfif 1 EQ 1>\r\n" +
				"	<cfset x=123/>\r\n" +
				"	<cfset x=555/>\r\n" +
				"	</cfif>\r\n" +
				"</cffunction>\r\n" +
				"<cffunction name=\"test2\">\r\n" +
				"	<cfif 1 EQ 1>\r\n" +
				"	<cfset x=123/>\r\n" +
				"	<cfset x=555/>\r\n" +
				"	</cfif>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
		assertEquals("MISSING_VAR",result.get(1).getMessageCode());
		assertEquals(10,result.get(1).getLine());
	}

	@Test
	public void testSimpleCFReadVar() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfif trim(Privilege) EQ \"\" or isUserInRole('#Privilege#')>\r\n" +
				"	<cfoutput>#x#</cfoutput>\r\n" +
				"	<cfset var y=x/>\r\n" +
				"	</cfif>" +
				"</cffunction>" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0,result.size());
	}

	@Test
	public void testSimpleCFSETArg() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"<cfargument name=\"x\" default=\"\">\r\n" +
				"	<cfif 1 EQ 1>\r\n" +
				"	<cfset x=123/>\r\n" +
				"	<cfset var y=123/>\r\n" +
				"	</cfif>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0,result.size());
	}	

	@Test
	public void testSimpleCFSETNoParse() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfset x=123/>" +
				"	<cfset var y=123/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());//Note parsing error fixed
		
	}

	@Test
	public void testSimpleCFSCRIPTCommentInScript() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfscript>\r\n" +
				"   // Set Rate Event Fields;\r\n" +
				"	var x={};\r\n" +
				"	</cfscript>\r\n" +
				"	<cfscript>\r\n" +
				"	x.xx=123;\r\n" +
				"	</cfscript>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals("" + result,0,result.size());
	}
	
	@Test
	public void testSimpleCFQUERYNested() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfoutput query=\"q123\">\r\n" +
				"	<cfset var y=123/>\r\n" +
				"	<cfoutput>#y#</cfoutput>\r\n" +
				"	</cfoutput>\r\n" +
				"</cffunction>\r\n" +
				"/<cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("NESTED_CFOUTPUT",result.get(0).getMessageCode());
		assertEquals(5,result.get(0).getLine());
	}
	
	@Test
	public void testSimpleCFQUERYIssue30() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfoutput query=\"qryPopups\" group=\"id\">\n" + 
				"            <cfif message NEQ \"\" and not(ListFind(arraytoList( arguments.quote.getErrorHandler().getShownErrors()),id))>\n" + 
				"                <cfoutput>#message####id#|</cfoutput>\n" + 
				"                <cfset arrayappend(arguments.quote.getErrorHandler().getShownErrors(),id)>\n" + 
				"            </cfif>\n" + 
				"            <cflog file=\"Quote_#quote.getOnlineNumber()#\" text=\"Popup:#message#,#id#\" type=\"informational\">\n" + 
				"        </cfoutput>\r\n" +
				"</cffunction>\r\n" +
				"/<cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().size());
	}
	
	@Test
	public void testSimpleCFQUERYIssue30b_OK() throws ParseException, IOException{
		final String cfcSrc = 
				"	<cfoutput query=\"qryPopups\" group=\"id\">\n" + 
				"	<cfoutput group=\"id2\">\n" + 
				"            <cfif message NEQ \"\" and not(ListFind(arraytoList( arguments.quote.getErrorHandler().getShownErrors()),id))>\n" + 
				"                <cfoutput>#message####id#|</cfoutput>\n" + 
				"                <cfset arrayappend(arguments.quote.getErrorHandler().getShownErrors(),id)>\n" + 
				"            </cfif>\n" + 
				"            <cflog file=\"Quote_#quote.getOnlineNumber()#\" text=\"Popup:#message#,#id#\" type=\"informational\">\n" + 
				"        </cfoutput>\r\n" +
				"        </cfoutput>\r\n" ;
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().size());
	}
	@Test
	public void testSimpleCFQUERYIssue30bNotOK() throws ParseException, IOException{
		final String cfcSrc = 
				"	<cfoutput query=\"qryPopups\" group=\"id\">\n" + 
				"	<cfoutput>\n" + 
				"            <cfif message NEQ \"\" and not(ListFind(arraytoList( arguments.quote.getErrorHandler().getShownErrors()),id))>\n" + 
				"                <cfoutput>#message####id#|</cfoutput>\n" + 
				"                <cfset arrayappend(arguments.quote.getErrorHandler().getShownErrors(),id)>\n" + 
				"            </cfif>\n" + 
				"            <cflog file=\"Quote_#quote.getOnlineNumber()#\" text=\"Popup:#message#,#id#\" type=\"informational\">\n" + 
				"        </cfoutput>\r\n" +
				"        </cfoutput>\r\n" ;
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("NESTED_CFOUTPUT",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());

	}


	@Test
	public void testSimpleCFQUERYNestedWithGroup() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfoutput query=\"q123\" group=\"x\">\r\n" +
				"	<cfset var y=123/>\r\n" +
				"	<cfoutput>#y#</cfoutput>\r\n" +
				"	</cfoutput>\r\n" +
				"</cffunction>\r\n" +
				"/<cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().size());
	}
	
	@Test
	public void testCFScriptCfc() throws ParseException, IOException{
		final String cfcSrc="/** \r\n" + 
				"* Simple Component. \r\n" + 
				"*/ \r\n" + 
				"component { \r\n" + 
				"/** \r\n" + 
				"* Simple function. \r\n" + 
				"*/ \r\n" + 
				"public void function foo() { \r\n" + 
				"xx=123; \r\n" + 
				"} \r\n" + 
				"}";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(9,result.get(0).getLine());
	}
	
	@Test
	public void testCFScriptCfcArgOk() throws ParseException, IOException{
		final String cfcSrc="/** \r\n" + 
				"* Simple Component. \r\n" + 
				"*/ \r\n" + 
				"component { \r\n" + 
				"/** \r\n" + 
				"* Simple function. \r\n" + 
				"*/ \r\n" + 
				"public void function foo(any arg1=\"\") { \r\n" + 
				"arg1=123; \r\n" + 
				"} \r\n" + 
				"}";
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0,result.size());
	}

	@Test
	public void testCFScriptIf() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfargument name=\"quote\">\r\n" + 
				"\r\n" + 
				"<cfscript>\r\n" + 
				"if (xx) {\r\n" + 
				"yy=123;\r\n" + 
				"}else{\r\n" + 
				"zz=123;\r\n" + 
				"}\r\n" + 
				"</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2,result.size());
	}
	
	
	@Test
	public void testCFScriptFor() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfargument name=\"quote\">\r\n" + 
				"\r\n" + 
				"<cfscript>\r\n" + 
				"for (i = 0; i < 100; i + 5) {\r\n" + 
				"xx=123;\r\n" + 
				"}\r\n" + 
				"</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2,result.size());
	}
	
	@Test
	public void testCFScriptDOT() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfscript>\r\n" + 
				"request.yy=123;\r\n" + 
				"</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		assertEquals(1,result.size());
		assertEquals("GLOBAL_VAR",result.get(0).getMessageCode());
	}
	
	@Test
	public void testCFScriptQueryNew() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"test\" >\r\n" + 
				"</cffunction>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfscript>\r\n" + 
				"var qry = QueryNew('A,B');\r\n" + 
				"</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("QUERYNEW_DATATYPE",result.get(0).getMessageCode());
		assertEquals(6,result.get(0).getLine());
	}
	
	@Test
	public void testCFScriptForDeclare() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"func1\">\r\n" + 
				"	<cfscript>\r\n" + 
			"			for (var a=1;a LTE arraylen(d); a=a+1) {\r\n" + 
			"				if(d[a].getName() EQ drawer){\r\n" + 
			"					return d[a];\r\n" + 
			"				}\r\n" + 
			"			}\r\n" + 
				"	</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().size());
	}

	@Test
	public void testCFScriptForUnVarred() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"func1\">\r\n" + 
				"	<cfscript>\r\n" + 
			"			for (a=1;a LTE arraylen(d); a=a+1) {\r\n" + 
			"				if(d[a].getName() EQ drawer){\r\n" + 
			"					return d[a];\r\n" + 
			"				}\r\n" + 
			"			}\r\n" + 
				"	</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}

	@Test
	@Ignore
	public void testCFScriptAStruct() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"func1\">\r\n" + 
				"	<cfscript>\r\n" + 
				"		var a = {};\r\n" + 
				"		a.response = processRequest(argumentCollection=arguments);\r\n" + 
				"	</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().size());
	}
	
	@Test
	public void testGlobalVarCheckerNPE() throws ParseException, IOException{
		cfBugs.process("component {\r\n" +
				"public any function process(){\r\n" +
				"url.x=123;\r\n" +
				"}\r\n"+
				"}","test");
		List<BugInfo> list = cfBugs.getBugs().getFlatBugList();
		System.out.println(list.toString());
        assertEquals(list.toString(),1,list.size());
		assertEquals("test",list.get(0).getFilename());
		assertEquals("process",list.get(0).getFunction());
		assertEquals(3,list.get(0).getLine());
	}
	
	@Test
	public void testVarScoper() throws ParseException, IOException{
		cfBugs.process("component {\r\n" +
				"public any function process(){\r\n" +
				"x=123;\r\n" +
				"}\r\n"+
				"}","test");
		List<BugInfo> list = cfBugs.getBugs().getFlatBugList();
		assertEquals(1,list.size());
		assertEquals("test",list.get(0).getFilename());
		assertEquals("process",list.get(0).getFunction());
		assertEquals(3,list.get(0).getLine());
	}
}
