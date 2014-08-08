package com.parser.src.cfml.parsing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.parser.main.CFMLParser;
import com.parser.main.cfscript.script.CFScriptStatement;

public class TestScriptReservedWordOrder {
	
	private CFMLParser fCfmlParser;
	
	@Before
	public void setUp() throws Exception {
		fCfmlParser = new CFMLParser();
	}
	
	private CFScriptStatement parseScript(String script) {
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("whoops! " + e.getMessage());
		}
		return scriptStatement;
	}
	
	@Test
	public void testFuncNameMatchesAccessType() {
		String script = "function package() {}";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testAccessTypeAndFuncNameMatch() {
		String script = "package function package() {}";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testReturnAndAccessTypeAndFuncNameMatch() {
		String script = "package package function package() {}";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
}
