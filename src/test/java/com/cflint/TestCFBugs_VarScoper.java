package com.cflint;
/**
 * tests from 
 * https://github.com/mschierberl/varscoper/blob/master/varScoper.cfc
 * 
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.VarScoper;

import cfml.parsing.cfscript.ParseException;

import static org.junit.Assert.*;

public class TestCFBugs_VarScoper {

	StackHandler handler = null;

	
	@Before
	public void setUp(){
		handler = new StackHandler();
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
		CFLint cfBugs = new CFLint(new VarScoper());
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
		CFLint cfBugs = new CFLint(new VarScoper());
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
		CFLint cfBugs = new CFLint(new VarScoper());
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
		CFLint cfBugs = new CFLint(new VarScoper());
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		assertEquals(1,cfBugs.getBugs().getBugList().size());
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals("yy",result.get(0).getVariable());
		assertEquals(3,result.get(0).getLine());
	}
	
}
