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

public class TestCFLint2Files {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("ArgDefChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("ARG_DEFAULT_MISSING");
		pluginMessage.setSeverity("WARNING");
		pluginMessage.setMessageText("Argument ${variable} is not required and does not define a default value.");
		pluginRule.getMessages().add(pluginMessage);

		pluginRule = new PluginInfoRule();
		pluginRule.setName("QueryParamChecker");
		conf.getRules().add(pluginRule);
		pluginMessage = new PluginMessage("QUERYPARAM_REQ");
		pluginMessage.setSeverity("WARNING");
		pluginMessage.setMessageText("setSql() statement should use .addParam() instead of #'s for security.");
		pluginRule.getMessages().add(pluginMessage);
		
		cfBugs = new CFLint(conf, new ArgDefChecker(),new QueryParamChecker());
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
 
		final String cfcSrc2 = "component {\n" + 
				"    public string function fooFunction() {\n" + 
				"        return foo = bar\n" + 
				"    }\n" + 
				"}";
		cfBugs.process(cfcSrc2, "test");
		final List<BugInfo> result2 = cfBugs.getBugs().getBugList().get("MISSING_SEMI");
		assertEquals(1, result2.size());
		System.out.println(result2);
		assertEquals(4, result2.get(0).getLine());
	}
}
