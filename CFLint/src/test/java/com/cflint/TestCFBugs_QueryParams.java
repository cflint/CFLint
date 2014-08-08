package com.cflint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.ArgDefChecker;
import com.cflint.plugins.core.QueryParamChecker;
import com.cflint.plugins.core.TypedQueryNew;
import com.cflint.plugins.core.VarScoper;

import com.parser.main.CFMLParser;
import com.parser.main.CFMLSource;
import com.parser.main.cfscript.ParseException;

import static org.junit.Assert.*;

public class TestCFBugs_QueryParams {

	StackHandler handler = null;
	
	@Before
	public void setUp(){
		handler = new StackHandler();
	}
	
	@Test
	public void testCFScript_QueryParams_OK() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfquery name=\"LOCAL.categories\">\r\n" + 
				"SELECT * FROM product_categories p\r\n" + 
				"WHERE p.id = <cfqueryparam value=\"#LOCAL.id#\"/>\r\n" + 
				"and p.name = <cfqueryparam value=\"abc\"/>\r\n" + 
				"</cfquery>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new QueryParamChecker());
		cfBugs.process(cfcSrc,"test");
		Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0,result.size());
	}
	
	
	@Test
	public void testCFScript_QueryParams_Script_OK() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" +
				"<cfscript>\r\n" +
				"local.query = new Query();" +
				"local.query.setSql(\"\r\n" + 
				"    SELECT id from table where id = :id\");" + 
				"local.query.addParam(name=\"id\", cfsqltype=\"CF_SQL_INTEGER\", value=arguments.id, maxlength=10);\r\n" + 
				"</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new QueryParamChecker());
		cfBugs.process(cfcSrc,"test");
		Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0,result.size());
	}
		
	@Test
	public void testCFScript_QueryParams_2Hashes() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfquery name=\"LOCAL.categories\">\r\n" + 
				"SELECT * FROM product_categories p\r\n" + 
				"WHERE p.id = #LOCAL.id#\r\n" + 
				"and p.name = #LOCAL.abc#\r\n" + 
				"</cfquery>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new QueryParamChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		for(BugInfo bi: result){
			System.out.println(bi);
		}
		assertEquals(2,result.size());
		assertEquals("QUERYPARAM_REQ",result.get(0).getMessageCode());
		assertEquals(3,result.get(0).getLine());
		assertEquals("LOCAL.id",result.get(0).getVariable());
		assertEquals("QUERYPARAM_REQ",result.get(1).getMessageCode());
		assertEquals(3,result.get(1).getLine());
	}
	@Test
	public void testCFScript_QueryParams_OutsideFunction() throws ParseException, IOException{
		final String cfcSrc="<cfquery name=\"LOCAL.categories\">\r\n" + 
				"SELECT * FROM product_categories p\r\n" + 
				"WHERE p.id = #LOCAL.id#\r\n" + 
				"and p.name = #LOCAL.abc#\r\n" + 
				"</cfquery>"; 
		CFLint cfBugs = new CFLint(new QueryParamChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		for(BugInfo bi: result){
			System.out.println(bi);
		}
		assertEquals(2,result.size());
		assertEquals("QUERYPARAM_REQ",result.get(0).getMessageCode());
		assertEquals(1,result.get(0).getLine());
		assertEquals("LOCAL.id",result.get(0).getVariable());
		assertEquals("QUERYPARAM_REQ",result.get(1).getMessageCode());
		assertEquals(1,result.get(1).getLine());
	}
	
	@Test
	public void testCFScript_QueryParams_Script_Hashes() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfscript>\r\n" +
				"local.query = new Query();" +
				"local.query.setSql(\"\r\n" + 
				"    SELECT id from table where id = #arguments.id#\");" + 
				"</cfscript>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new QueryParamChecker());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		for(BugInfo bi: result){
			System.out.println(bi);
		}
		assertEquals(1,result.size());
		assertEquals("QUERYPARAM_REQ",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
		assertEquals("local.query",result.get(0).getVariable());
	}
	
	@Test
	public void testCFScript_QueryParams_EscapeHashes() throws ParseException, IOException{
		final String cfcSrc="<cfcomponent>\r\n" +
				"<cffunction name=\"rateBop\" >\r\n" + 
				"<cfquery name=\"LOCAL.categories\">\r\n" + 
				"SELECT * FROM product_categories p\r\n" + 
				"WHERE p.##id = 1\r\n" + 
				"</cfquery>\r\n" + 
				"</cffunction>" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new QueryParamChecker());
		cfBugs.process(cfcSrc,"test");
		Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0,result.size());
	}
}
