package com.cflint;

/**
 * tests from 
 * https://github.com/mschierberl/varscoper/blob/master/varScoper.cfc
 * 
 */
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cfml.parsing.cfscript.ParseException;

import com.cflint.plugins.core.VarScoper;

@RunWith(Parameterized.class)
public class TestCFBugs_VarScoper_Names {

	StackHandler handler = null;
	final String tagName;

	@Before
	public void setUp() {
		handler = new StackHandler();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection primeNumbers() {
		return Arrays.asList(new String[][] { new String[] { "CFStoredProc" }, new String[] { "CFQuery" },
				new String[] { "CFFeed" }, new String[] { "CFDirectory" }, new String[] { "CFForm" },
				new String[] { "CFFtp" }, new String[] { "CFObject" }, new String[] { "CFSearch" },
				new String[] { "CFProcResult" }, new String[] { "CFPop" }, new String[] { "CFRegistry" },
				new String[] { "CFReport" }, new String[] { "CFDBInfo" }, new String[] { "CFDocument" },
				new String[] { "CFCollection" }, new String[] { "CFPdf" }, new String[] { "CFZip" },
				new String[] { "CFLdap" } });
		// return Arrays.asList(new String[] { new String[]{"CFStoredProc",
		// "CFFeed", "CFFtp", "CFObject", "CFSearch",
		// "CFProcResult", "CFPop", "CFRegistry", "CFReport", "CFDBInfo",
		// "CFDocument", "CFCollection", "CFPdf",
		// "CFZip", "CFLdap" });
	}

	public TestCFBugs_VarScoper_Names(final String tagName) {
		super();
		this.tagName = tagName;
	}

	@Test
	public void testScope_Name() throws ParseException, IOException {
		runTagAttrTest(tagName.toLowerCase(), "name", "xx");
		runTagAttrTest(tagName, "Name", "xx");
	}

	@Test
	public void testScope_Name_Vard() throws ParseException, IOException {
		runTagAttrTestVard(tagName.toLowerCase(), "name", "xx");
		runTagAttrTestVard(tagName, "Name", "xx");
	}

	public void runTagAttrTest(final String tag, final String attr, final String variable) throws ParseException,
			IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <" + tag + " " + attr
				+ "=\"" + variable + "\">\r\n" + "	</" + tag + ">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
		List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("MISSING_VAR", result.get(0).getMessageCode());
		assertEquals(variable, result.get(0).getVariable());
		assertEquals(3, result.get(0).getLine());
	}

	public void runTagAttrTestVard(final String tag, final String attr, final String variable) throws ParseException,
			IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + 
			"<cffunction name=\"test\">\r\n" +
			"   <cfset var " + variable + "=123/>\r\n" +
			"   <" + tag + " " + attr + "=\"" + variable + "\">\r\n" + 
			"   </" + tag + ">\r\n" + 
			"</cffunction>\r\n" + "</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}
}
