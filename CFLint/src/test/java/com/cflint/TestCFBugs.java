package com.cflint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.NestedCFOutput;
import com.cflint.plugins.core.TypedQueryNew;
import com.cflint.plugins.core.VarScoper;

import com.parser.main.CFMLParser;
import com.parser.main.CFMLSource;
import com.parser.main.cfscript.ParseException;

import static org.junit.Assert.*;
import org.junit.Ignore;

public class TestCFBugs {

	StackHandler handler = null;
	
	@Before
	public void setUp(){
		handler = new StackHandler();
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}
	
	@Test
	public void testSimpleCFSET_FirstOffenseOnly() throws ParseException, IOException{
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
		assertEquals("MISSING_VAR",result.get(1).getMessageCode());
		assertEquals(10,result.get(1).getLine());
	}

	@Test
	public void testSimpleCF_ReadVar() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfif trim(Privilege) EQ \"\" or isUserInRole('#Privilege#')>\r\n" +
				"	<cfoutput>#x#</cfoutput>\r\n" +
				"	<cfset var y=x/>\r\n" +
				"	</cfif>" +
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0,result.size());
	}

	@Test
	public void testSimpleCFSET_Arg() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"<cfargument name=\"x\" default=\"\">\r\n" +
				"	<cfif 1 EQ 1>\r\n" +
				"	<cfset x=123/>\r\n" +
				"	<cfset var y=123/>\r\n" +
				"	</cfif>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0,result.size());
	}	

	@Test
	public void testSimpleCFSET_NoParse() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfset x=123/" +
				"	<cfset var y=123/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("PARSE_ERROR",result.get(0).getMessageCode());
		
	}

	@Test
	public void testSimpleCFSCRIPT_CommentInScript() throws ParseException, IOException{
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals("" + result,0,result.size());
	}
	
	@Test
	public void testSimpleCFQUERY_nested() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfoutput query=\"q123\">\r\n" +
				"	<cfset var y=123/>\r\n" +
				"	<cfoutput>#y#</cfoutput>\r\n" +
				"	</cfoutput>\r\n" +
				"</cffunction>\r\n" +
				"/<cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper(), new NestedCFOutput());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("NESTED_CFOUTPUT",result.get(0).getMessageCode());
		assertEquals(5,result.get(0).getLine());
	}

	@Test
	public void testSimpleCFQUERY_nestedWithGroup() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfoutput query=\"q123\" group=\"x\">\r\n" +
				"	<cfset var y=123/>\r\n" +
				"	<cfoutput>#y#</cfoutput>\r\n" +
				"	</cfoutput>\r\n" +
				"</cffunction>\r\n" +
				"/<cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper(), new NestedCFOutput());
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().size());
	}
	
	@Test
	public void testCFScript_Cfc() throws ParseException, IOException{
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(8,result.get(0).getLine());
	}
	
	@Test
	public void testCFScript_Cfc_ArgOk() throws ParseException, IOException{
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0,result.size());
	}

	@Ignore
	@Test
	public void testParse2() throws IOException{
		CFMLSource cfmlSource = new CFMLSource(loadFile("C:\\source\\cfmx\\extensions\\components\\goodville\\process\\quote\\df\\dwellingFireManager.cfc"));
		CFMLParser parser = new CFMLParser();
		parser.parseElements(cfmlSource);
	}

	private String loadFile(String fileString) {
		try {
			FileInputStream fis = new FileInputStream(fileString);
			byte b[] = new byte[fis.available()];
			fis.read(b);
			return new String(b);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Test
	public void testCFScript_If() throws ParseException, IOException{
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		for(BugInfo bi: result){
			System.out.println(bi);
		}
		assertEquals(2,result.size());
	}
	
	
	@Test
	public void testCFScript_For() throws ParseException, IOException{
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		for(BugInfo bi: result){
			System.out.println(bi);
		}
		assertEquals(2,result.size());
	}
	
	@Test
	public void testCFScript_DOT() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfscript>\r\n" + 
				"request.yy=123;\r\n" + 
				"</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0,result.size());
	}
	
	@Test
	public void testCFScript_QueryNew() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfscript>\r\n" + 
				"var qry = QueryNew('A,B');\r\n" + 
				"</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper(), new TypedQueryNew());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		for(BugInfo bi: result){
			System.out.println(bi);
		}
		assertEquals(1,result.size());
		assertEquals("QUERYNEW_DATATYPE",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}
	
	@Test
	public void testCFScript_ForDeclare() throws ParseException, IOException{
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().size());
	}

	@Test
	public void testCFScript_ForUnVarred() throws ParseException, IOException{
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}

	@Test
	public void testCFScript_a_struct() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"func1\">\r\n" + 
				"	<cfscript>\r\n" + 
				"		var a = {};\r\n" + 
				"		a.response = processRequest(argumentCollection=arguments);\r\n" + 
				"	</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		assertEquals(0,cfBugs.getBugs().size());
	}
	
}
