package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import cfml.parsing.reporting.ParseException;

public class TestCreateObjectChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("CreateObjectChecker");
		cfBugs = new CFLint(conf);
	}

	@Test
	public void testCreateObjectComponent() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "var obj = createObject(\"component\",\"componentPath\");\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_CREATEOBJECT", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void testCreateObjectComponentMultiLine() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "var obj = createObject(\r\n"
			+"\"component\",\r\n"
			+ "\"componentPath\");\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("AVOID_USING_CREATEOBJECT", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void testCreatObjectJava() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "var a = 23;\r\n"
			+ "var obj = createObject(\"java\",\"javaPath\");\r\n"
			+ "</cfscript>";
		
		cfBugs.process(scriptSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

}
