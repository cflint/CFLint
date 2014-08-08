package com.parser.src.cfml.parsing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.parser.main.CFMLParser;
import com.parser.main.cfscript.script.CFScriptStatement;

public class TestScriptParserIfs {
	
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
	public void testIfLongFunkKeyWord() {
		String script = "if(tag.template.startsWith('ram:')) {}";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testIfLongFunk() {
		String script = "if(foo.bar.blah('ram:')) {}";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testIfshortForm() {
		String script = "if ( structKeyExists( cfc, \"get#property#\" ) ) return evaluate( 'cfc.get#property#()' );";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
}
