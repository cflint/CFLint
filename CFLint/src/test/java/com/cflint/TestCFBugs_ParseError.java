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
import com.cflint.plugins.core.VarScoper;

import com.parser.main.CFMLParser;
import com.parser.main.CFMLSource;
import com.parser.main.cfscript.ParseException;

import static org.junit.Assert.*;

public class TestCFBugs_ParseError {

	StackHandler handler = null;
	
	@Before
	public void setUp(){
		handler = new StackHandler();
	}
	
	@Test
	public void testSimpleCFSET() throws ParseException, IOException{
		final String cfcSrc = "<cfcomponent>\r\n" +
				"<cffunction name=\"test\">\r\n" +
				"	<cfset var x={}/>\r\n" +
				"	<cfset var x.y=123/>\r\n" +
				"</cffunction>\r\n" +
				"</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("PARSE_ERROR",result.get(0).getMessageCode());
		assertEquals(4,result.get(0).getLine());
	}
}
