package com.parser.src.cfml.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.StartTag;

import org.junit.Before;
import org.junit.Test;

import com.parser.main.CFMLParser;
import com.parser.main.CFMLSource;
import com.parser.main.ParserTag;
import com.parser.main.cfscript.script.CFScriptStatement;

public class TestCFMLParser {
	
	private CFMLParser fCfmlParser;
	private static final String sourceUrlFile = "file:test/data/cfml/test1.cfm";
	
	@Before
	public void setUp() throws Exception {
		fCfmlParser = new CFMLParser();
		try {
			fCfmlParser.addCFMLSource(new URL(sourceUrlFile));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fCfmlParser.parse();
	}
	
	@Test
	public void testGetCFMLTags() {
		ArrayList<StartTag> elementList = fCfmlParser.getCFMLTags();
		// ArrayList<StartTag> elementList = fCfmlParser.getAllTags();
		for (StartTag element : elementList) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println(element.getDebugInfo());
			if (element.getAttributes() != null) {
				System.out.println("XHTML StartTag:\n" + element.tidy(true));
				System.out.println("Attributes:\n" + element.getAttributes());
			}
			System.out.println("Source text with content:\n" + element);
		}
		System.out.println(fCfmlParser.printMessages());
		assertEquals(14, elementList.size());
	}
	
	@Test
	public void testGetDebugInfo() {
		String debugInfo = fCfmlParser.getDebuggingInfo();
		System.out.println(debugInfo);
		assertEquals(21, 21);
	}
	
	@Test
	public void testGetTagAt() {
		String path = "";
		try {
			path = new URL(sourceUrlFile).getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// has to be the exact start pos of the tag
		ParserTag parserTag = fCfmlParser.getCFMLSource(path).getTagAt(8);
		System.out.println(fCfmlParser.printMessages());
		assertEquals("cffunction", parserTag.getName());
	}
	
	@Test
	public void testGetEnclosingTag() {
		String path = "";
		try {
			path = new URL(sourceUrlFile).getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParserTag parserTag = fCfmlParser.getCFMLSource(path).getEnclosingTag(355);
		System.out.println(fCfmlParser.printMessages());
		assertEquals("cffunction", parserTag.getName());
	}
	
	@Test
	public void testGetNextTag() {
		String path = "";
		try {
			path = new URL(sourceUrlFile).getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParserTag parserTag = fCfmlParser.getCFMLSource(path).getNextTag(355);
		System.out.println(fCfmlParser.printMessages());
		assertEquals("cfargument", parserTag.getName());
	}
	
	@Test
	public void testGetPreviousTag() {
		String path = "";
		try {
			path = new URL(sourceUrlFile).getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParserTag parserTag = fCfmlParser.getCFMLSource(path).getPreviousTag(355);
		System.out.println(fCfmlParser.printMessages());
		assertEquals("cfquery", parserTag.getName());
	}
	
	@Test
	public void testGetCFMLSource() {
		String path = "";
		try {
			path = new URL(sourceUrlFile).getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CFMLSource cfmlSource = fCfmlParser.getCFMLSource(path);
		assertNotNull(cfmlSource);
	}
	
	@Test
	public void testParseScript() {
		String path = "";
		String script = "var x = 1; y = 5; createObject('java','java.lang.String');";
		try {
			path = new URL(sourceUrlFile).getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public void testParseScriptMissingSemiColon() {
		String path = "";
		String script = "var x = 1; y = 5 createObject('java','java.lang.String');";
		try {
			path = new URL(sourceUrlFile).getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(fCfmlParser.printMessages());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptMissingAssignment() {
		String script = "var x = 1; y =; createObject('java','java.lang.String');";
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
	public void testParseScriptTernary() {
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
	public void testParseScriptTernary2() {
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
	
	@Test
	public void testParseScriptBadLex() {
		String path = "";
		String script = "string function fart(required kind, area='elavator') {"
				+ "var toot = 'se5ee6yye67tutuityit69t9imfuihki';"
				+ "var registry = createObject('java','org.eclipse.emf.ecore.EPackage$Registry').INSTANCE;"
				+ "var className = listLast(class,'/');" + "var packageName = '';" + "if(isObject(class)) {"
				+ "this._instance = class;" + "} else {" + "weee" + "}};";
		try {
			path = new URL(sourceUrlFile).getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptCfcGood() {
		String path = "";
		try {
			path = new URL("file:test/data/cfml/ScriptComponent.cfc").getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScriptFile(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptCfcHarder() {
		String path = "";
		try {
			path = new URL("file:test/data/cfml/ScriptComponentHarder.cfc").getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScriptFile(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptCfcWow() {
		String path = "";
		try {
			path = new URL("file:test/data/cfml/ScriptComponentWow.cfc").getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CFScriptStatement scriptStatement = null;
		try {
			scriptStatement = fCfmlParser.parseScriptFile(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptFunction() {
		String script = "function runFunction(functionName,argCol) { runFunk = this[functionName]; results = structNew(); results.result = runFunk(argumentCollection=argCol); results.debug = getDebugMessages(); return results; }";
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
	public void testParseScriptTryCatch() {
		String script = "try { throw('funk'); } catch (Any e) { woot(); }";
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
	public void testGetAllTags() {
		List<Element> elementList = fCfmlParser.getAllTags();
		for (Element element : elementList) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println(element.getDebugInfo());
			if (element.getAttributes() != null) {
				System.out.println("XHTML StartTag:\n" + element.getStartTag().tidy(true));
				System.out.println("Attributes:\n" + element.getAttributes());
			}
			System.out.println("Source text with content:\n" + element);
		}
		System.out.println(fCfmlParser.getCacheDebugInfo());
		System.out.println(fCfmlParser.printMessages());
		assertEquals(21, elementList.size());
	}
	
}
