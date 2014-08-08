package com.parser.src.cfml.parsing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.parser.main.CFMLParser;
import com.parser.main.cfscript.script.CFScriptStatement;

public class TestScriptParser {
	
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
	public void testParseScript() {
		String script = "var x = 1; y = 5; createObject('java','java.lang.String');";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptMissingSemiColon() {
		String script = "var x = 1; y = 5 createObject('java','java.lang.String');";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() == 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		System.out.println(fCfmlParser.printMessages());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptMissingAssignment() {
		String script = "var x = 1; y =; createObject('java','java.lang.String');";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() == 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testForIn() {
		String script = "for(widget in thingWithWidgets.getWidgets()) { writeOutput(widget); }";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testNewOperator() {
		String script = "datatypes = new CFDataTypes().package();";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
		script = "datatypes = new CFDataTypes().package().member;";
		scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
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
	public void testParseScriptBadLex() {
		String script = "string function fart(required kind, area='elavator') {"
				+ "var toot = 'se5ee6yye67tutuityit69t9imfuihki';"
				+ "var registry = createObject('java','org.eclipse.emf.ecore.EPackage$Registry').INSTANCE;"
				+ "var className = listLast(class,'/');" + "var packageName = '';" + "if(isObject(class)) {"
				+ "this._instance = class; } else { weee }};";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() == 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
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
			fail("whoops! " + e.getMessage());
		}
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
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
			fail("whoops! " + e.getMessage());
		}
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
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
			fail("whoops! " + e.getMessage());
		}
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptCfcCrazy() {
		String path = "";
		try {
			path = new URL("file:test/data/cfml/ScriptComponentCrazy.cfc").getPath();
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
			fail("whoops! " + e.getMessage());
		}
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptFw1() {
		String path = "";
		try {
			path = new URL("file:test/data/cfml/fw1.cfc").getPath();
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
			fail("whoops! " + e.getMessage());
		}
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseCFCWithColonMetadata() {
		String path = "";
		try {
			path = new URL("file:test/data/cfml/CFCWithColonMetadata.cfc").getPath();
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
			fail("whoops! " + e.getMessage());
		}
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		System.out.println(scriptStatement.toString());
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseAllTestCFCs() {
		String path = "";
		try {
			path = new URL("file:test/data/cfml/").getPath();
			File[] files = new File(path).listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.println("Directory: " + file.getName());
				} else if (file.getPath().endsWith(".cfc")) {
					CFScriptStatement scriptStatement = null;
					try {
						scriptStatement = fCfmlParser.parseScriptFile(file.getAbsolutePath());
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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParseScriptFunction() {
		String script = "function runFunction(functionName,argCol) { runFunk = this[functionName]; results = structNew(); results.result = runFunk(argumentCollection=argCol); results.debug = getDebugMessages(); return results; }";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptEmptyCompDecl() {
		String script = "component { }";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseCompAsArgType() {
		String script = "void function setChild(required component child)  { ArrayAppend(this.children,child); }";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testFunctionWithNamedParamNamedNull() {
		String script = "oNewStarters.addParam(name=\"pager\", null=true, cfsqltype=\"cf_sql_varchar\");";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testFunctionWithRestMetadata() {
		String script = "remote User function getUser(numeric userid restargsource=\"Path\") httpmethod=\"GET\" restpath=\"{userid}\" {}";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testFunctionWithDefaultAndRestMetadata() {
		String script = "remote User function getUser(numeric userid=\"default\" restargsource=\"Path\") httpmethod=\"GET\" restpath=\"{userid}\" {}";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void functionCallHashedParamsSideBySide() {
		String script = "arrayAppend( variables.framework.routes, { '#method##route#' : target } );";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void propertyKeyWordButNotProperty() {
		String script = "public void function onPopulateError( any cfc, string property, struct rc ){}";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void structKeyHashedSideBySide() {
		String script = "funkstruct =  { '#method##route#' : target };";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testParseScriptTryCatch() {
		String script = "try { throw('funk'); } catch (Any e) { woot(); }";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
	@Test
	public void testVoidFunctionComplex() {
		String script = "public void function redirect( string action, string preserve = 'none', string append = 'none', string path = variables.magicBaseURL, string queryString = '', string statusCode = '302' ) { }";
		CFScriptStatement scriptStatement = parseScript(script);
		if (fCfmlParser.getMessages().size() > 0) {
			fail("whoops! " + fCfmlParser.getMessages());
		}
		
		assertNotNull(scriptStatement);
	}
	
}
