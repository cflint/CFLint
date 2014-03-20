package com.cflint;

/**
 * tests from 
 * https://github.com/mschierberl/varscoper/blob/master/varScoper.cfc
 * 
 */
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cfml.parsing.cfscript.ParseException;

import com.cflint.plugins.core.VarScoper;

@RunWith(Parameterized.class)
public class TestCFBugs_VarScoper_TagAttr {

	StackHandler handler = null;
	final String tagName;
	final String attribute;

	@Before
	public void setUp() {
		handler = new StackHandler();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection<String[]> primeNumbers() {
		List<String[]> retval = new ArrayList<String[]>();
		//retval.add(new String[] { "CFLoop", "Index" });
		//retval.add(new String[] { "CFLoop", "Item" });
		retval.add(new String[] { "CFInvoke", "ReturnVariable" });
		retval.add(new String[] { "CFFile", "Variable" });
		retval.add(new String[] { "CFSavecontent", "Variable" });
		retval.add(new String[] { "CFHttp", "Result" });
		retval.add(new String[] { "CFQuery", "Result" });
		retval.add(new String[] { "CFMail", "Query" });
		retval.add(new String[] { "CFFtp", "Result" });
		retval.add(new String[] { "CFWddx", "Output" });
		retval.add(new String[] { "CFExecute", "Variable" });
		retval.add(new String[] { "CFNtAuthenticate", "Result" });
		retval.add(new String[] { "CFXml", "Variable" });
		return retval;
	}

	public TestCFBugs_VarScoper_TagAttr(final String tagName, final String attribute) {
		super();
		this.tagName = tagName;
		this.attribute = attribute;
	}

	@Test
	public void testUnvarrd() throws ParseException, IOException {
		runTagAttrTest(tagName.toLowerCase(), attribute.toLowerCase(), "xx");
		runTagAttrTest(tagName, attribute, "xx");
	}
	@Test
	public void testVarrd() throws ParseException, IOException {
		runTagAttrTestVard(tagName.toLowerCase(), attribute.toLowerCase(), "xx");
		runTagAttrTestVard(tagName, attribute, "xx");
	}
	
	@Test
	public void testDotVarrd() throws ParseException, IOException {
		runTagAttrDotVarTest(tagName.toLowerCase(), attribute.toLowerCase(), "zz.xx","zz");
		runTagAttrDotVarTest(tagName, attribute, "zz.xx", "zz");
		runTagAttrDotVarTest(tagName, attribute, "this.xx", "zz");
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
	
	public void runTagAttrDotVarTest(final String tag, final String attr, final String variable, final String initVar) throws ParseException,
		IOException {
		System.out.println("test tag: " +tag);
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var " + initVar
				+ "=123/>\r\n" + "   <" + tag + " " + attr + "=\"" + variable + "\">\r\n" + "   </" + tag + ">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
	cfBugs.process(cfcSrc, "test");
	assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

	public void runTagAttrTestVard(final String tag, final String attr, final String variable) throws ParseException,
			IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var " + variable
				+ "=123/>\r\n" + "   <" + tag + " " + attr + "=\"" + variable + "\">\r\n" + "   </" + tag + ">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		CFLint cfBugs = new CFLint(new VarScoper());
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}
}
