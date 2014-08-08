package com.parser.src.cfml.parsing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.parser.main.CFMLParser;
import com.parser.main.cfscript.script.CFScriptStatement;

public class TestScriptSwitch {
	
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
	public void testSwitch() {
		String script = "switch(prop.getType()) {case 'date' : case 'datetime' : kronk=sqronk; break; default: flur;}";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testSwitchEmpty() {
		String script = "switch(prop) {case 'trunk' : case 'pleck' : break; case 'strunk' : break;}";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testCfmlFunctionStatement() {
		String script = "savecontent variable='renderedcontent' {model = duplicate(_model);metadata = duplicate(_model);};";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
}
