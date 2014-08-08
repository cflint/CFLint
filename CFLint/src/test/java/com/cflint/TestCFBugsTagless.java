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

public class TestCFBugsTagless {

	StackHandler handler = null;
	
	@Before
	public void setUp(){
		handler = new StackHandler();
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
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc,"test");
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1,result.size());
		assertEquals("MISSING_VAR",result.get(0).getMessageCode());
		assertEquals(6,result.get(0).getLine());
		assertEquals("myvar",result.get(0).getVariable());
	}
	}
