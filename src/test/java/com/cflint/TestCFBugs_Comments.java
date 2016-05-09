package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.ArgDefChecker;
import com.cflint.plugins.core.QueryParamChecker;

import cfml.parsing.reporting.ParseException;

public class TestCFBugs_Comments {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("ArgDefChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("ARG_DEFAULT_MISSING");
		pluginMessage.setSeverity("WARNING");
		pluginMessage.setMessageText("Argument ${variable} is not required and does not define a default value.");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new ArgDefChecker());
	}

	@Test
	public void testVarAndArgs_Disabled() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
				+ "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING --->"
				+ "<cfargument name=\"xyz\">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}
	@Test
	public void testVarAndArgs_DisabledSpacing() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
				+ "<!---    CFLINT-DISABLE  ARG_DEFAULT_MISSING   --->"
				+ "<cfargument name=\"xyz\">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}
	@Test
	public void testVarAndArgs_DisabledCase() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
				+ "<!---cflint-disable ARG_DEFAULT_MISSING   --->"
				+ "<cfargument name=\"xyz\">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testVarAndArgs_Disabled2() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
				+ "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING ,XXX --->"
				+ "<cfargument name=\"xyz\">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}
	
	@Test
	public void testVarAndArgs_DisabledOnParent() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING --->"
				+ "<cffunction name=\"test\">\r\n" + "	"
				+ "<cfargument name=\"xyz\">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testVarAndArgs_DisabledOnParent2() throws ParseException, IOException {
		final String cfcSrc = "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING ---><cfcomponent>\r\n" 
				+ "<cffunction name=\"test\">\r\n" + "	"
				+ "<cfargument name=\"xyz\">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testVarAndArgs_DisabledOther() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
				+ "<!---CFLINT-DISABLE SOMEOTHER--->"
				+ "<cfargument name=\"xyz\">\r\n"
				+ "</cffunction>\r\n" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARG_DEFAULT_MISSING", result.get(0).getMessageCode());
	}
	
	@Test
	public void testCFScript_QueryParams_Script_Hashes() throws ParseException, IOException {
		final ConfigRuntime conf = new ConfigRuntime();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("QueryParamChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("QUERYPARAM_REQ");
		pluginMessage.setSeverity("WARNING");
		pluginMessage.setMessageText("setSql() statement should use .addParam() instead of #'s for security.");
		pluginRule.getMessages().add(pluginMessage);
		cfBugs = new CFLint(conf, new QueryParamChecker());
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n" + "<!---CFLINT-DISABLE QUERYPARAM_REQ---><cfscript>\r\n"
				+ "local.query = new Query();" + "local.query.setSql(\"\r\n"
				+ "    SELECT id from table where id = #arguments.id#\");" + "</cfscript>\r\n" + "</cffunction>"
				+ "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}
	@Test
	public void testCFScript_QueryParams_Script_HashesParent() throws ParseException, IOException {
		final ConfigRuntime conf = new ConfigRuntime();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("QueryParamChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("QUERYPARAM_REQ");
		pluginMessage.setSeverity("WARNING");
		pluginMessage.setMessageText("setSql() statement should use .addParam() instead of #'s for security.");
		pluginRule.getMessages().add(pluginMessage);
		cfBugs = new CFLint(conf, new QueryParamChecker());
		final String cfcSrc = "<!---CFLINT-DISABLE QUERYPARAM_REQ---><cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n" + "<cfscript>\r\n"
				+ "local.query = new Query();" + "local.query.setSql(\"\r\n"
				+ "    SELECT id from table where id = #arguments.id#\");" + "</cfscript>\r\n" + "</cffunction>"
				+ "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

}
