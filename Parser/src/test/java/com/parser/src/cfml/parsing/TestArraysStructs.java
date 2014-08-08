package com.parser.src.cfml.parsing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.parser.main.CFMLParser;
import com.parser.main.cfscript.script.CFScriptStatement;

public class TestArraysStructs {
	
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
	public void testEmptyVaredArray() {
		String script = "var someArry = [];";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testVaredArrayOfStruct() {
		String script = "var someArry = [{funk:'wee'},{funk2:'wee2'}];";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testVaredArrayOfStructKyeyWerd() {
		String script = "var package = { name:'cfcPackage', datatypes:datatypes, classes : [{ name:'CFCModel',features : [{name='package', etype:'EString', lowerBound:0,upperBound:1},{name='cfcs', reference:'ORMEntity',containment:true, lowerBound:0,upperBound:-1}]}]};";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testStructOfArray() {
		String script = "var someStrucsArry = {stru:[{funk:'wee'},{funk2:'wee2'}], stro:[{funk:'wee'},{funk2:'wee2'}]};";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testArrayWithFunction() {
		String script = "arrData[ ArrayLen( arrData ) + 1 ] = { Foo = \"Bar\" };";
		CFScriptStatement scriptStatement = null;
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseNestedStructsAndArrays() {
		String path = "";
		try {
			path = new URL("file:test/data/cfml/NestedArraysStructs.cfc").getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScriptFile(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("whoops! " + e.getMessage());
		}
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
}
