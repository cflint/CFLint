package com.parser.src.cfml.parsing;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.parser.main.CFMLParser;
import com.parser.main.cfscript.script.CFScriptStatement;

public class TestScriptTernary {
	
	private CFMLParser fCfmlParser;
	private static final String sourceUrlFile = "file:test/data/cfml/test1.cfm";
	
	@Before
	public void setUp() throws Exception {
		fCfmlParser = new CFMLParser();
	}
	
	@Test
	public void testParseScriptTernaryFunction() {
		String script = "result = fileExists(destfile) ? \"overwritten\" : \"created\";";
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptTernaryFunctionParen() {
		String script = "result = (fileExists(destfile)) ? \"overwritten\" : \"created\";";
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptTernaryString() {
		// String script = "result = (fileExists(destfile)) ? \"overwritten\" : \"created\";";
		String script = "result = a == b ? \"overwritten\" : \"created\";";
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptTernaryChain() {
		String script = "result = a == b ? c > a ? 'c > a' : 'a > c' : 'b != a';";
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(scriptStatement);
	}
	
}
