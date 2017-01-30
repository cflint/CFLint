package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.OutputParmMissing;

import cfml.parsing.reporting.ParseException;

public class TestCFBugs_OutputDef {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final CFLintConfig conf = new CFLintConfig();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("OutputParmMissing");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("OUTPUT_ATTR");
		pluginMessage.setSeverity("INFO");
		pluginMessage.setMessageText("<${tag} name=\"${variable}\"> should have @output='false' ");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new OutputParmMissing());
	}


//	@Test
//	public void testCFCDef() throws ParseException, IOException {
//		final String cfcSrc = "<cfcomponent >\r\n" + "<cffunction name=\"test\" output=\"false\">\r\n"
//				+ "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
//				+ "</cfcomponent>";
//		final CFBugs cfBugs = new CFBugs(new OutputParmMissing());
//		cfBugs.process(cfcSrc, "test");
//		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
//		assertEquals(1, result.size());
//		assertEquals("OUTPUT_ATTR", result.get(0).getMessageCode());
//		assertEquals(2, result.get(0).getLine());
//	}

	@Test
	public void testFunctionDef() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent output=\"false\">\r\n" + "<cffunction name=\"test\">\r\n"
				+ "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
				+ "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("OUTPUT_ATTR", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("<cffunction name=\"test\"> should have @output='false'", result.get(0).getMessage());
	}

	@Test
	public void test_OK() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent output=\"false\">\r\n" + "<cffunction name=\"test\" output=\"false\">\r\n"
				+ "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
				+ "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

}
