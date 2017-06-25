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

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.VarScoper;

import cfml.parsing.reporting.ParseException;

@RunWith(Parameterized.class)
public class TestCFBugs_VarScoper_TagAttr {

	final String tagName;
	final String attribute;

	private CFLint cfBugs;
	
	@Before
	public void setUp(){
		CFLintConfig conf = new CFLintConfig();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("VarScoper");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("MISSING_VAR");
		pluginMessage.setSeverity("ERROR");
		pluginMessage.setMessageText("Variable ${variable} is not declared with a var statement.");
		pluginRule.getMessages().add(pluginMessage);
		
		cfBugs = new CFLint(conf,new VarScoper());
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
		setUp();
		runTagAttrTest(tagName, attribute, "xx");
		assertEquals(true,true); //Codacity 
	}
	@Test
	public void testVarrd() throws ParseException, IOException {
		runTagAttrTestVard(tagName.toLowerCase(), attribute.toLowerCase(), "xx");
		setUp();
		runTagAttrTestVard(tagName, attribute, "xx");
		assertEquals(true,true); //Codacity 
	}
	
	@Test
	public void testDotVarrd() throws ParseException, IOException {
		runTagAttrDotVarTest(tagName.toLowerCase(), attribute.toLowerCase(), "zz.xx","zz");
		setUp();
		runTagAttrDotVarTest(tagName, attribute, "zz.xx", "zz");
		setUp();
		runTagAttrDotVarTest(tagName, attribute, "this.xx", "zz");
		assertEquals(true,true); //Codacity 
	}

	public void runTagAttrTest(final String tag, final String attr, final String variable) throws ParseException,
			IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <" + tag + " " + attr
				+ "=\"" + variable + "\">\r\n" + "	</" + tag + ">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
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
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var " + initVar
				+ "=123/>\r\n" + "   <" + tag + " " + attr + "=\"" + variable + "\">\r\n" + "   </" + tag + ">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

	public void runTagAttrTestVard(final String tag, final String attr, final String variable) throws ParseException,
			IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var " + variable
				+ "=123/>\r\n" + "   <" + tag + " " + attr + "=\"" + variable + "\">\r\n" + "   </" + tag + ">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}
}
