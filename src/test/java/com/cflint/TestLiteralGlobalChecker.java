package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.LiteralChecker;

public class TestLiteralGlobalChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("LiteralChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN");
		pluginMessage.setSeverity("WARNING");
		LiteralChecker checker = new LiteralChecker();
		checker.setParameter("maximum", "3");
		checker.setParameter("maxWarnings", "2");
		checker.setParameter("scope", "global");
		cfBugs = new CFLint(conf, checker);
	}

	@Test
	public void testOK() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "var pi = 3.14;\r\n"
			+ "var code = \"CODE\";\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testTooManyLocalHardCodevedValues() throws ParseException, IOException {
		final String scriptSrc = "<cfscript>\r\n"
			+ "pi = 3.14;\r\n"
			+ "calc = \"AREA\";\r\n"
			+ "if (code == \"AREA\") {\r\n"
			+ "area = 3.14 * radius * radius;\r\n"
			+ "circumference = 2 * 3.14 * radius;\r\n"
			+ "volume = 4/3 * 3.14 * radius * radius * radius;\r\n"
			+"}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN", result.get(0).getMessageCode());
		assertEquals(7, result.get(0).getLine());
	}

		@Test
	public void testTooManyGlobalHardCodevedValues() throws ParseException, IOException {
		String scriptSrc = "<cfscript>\r\n"
			+ "pi = 3.14;\r\n"
			+ "calc = \"AREA\";\r\n"
			+ "if (code == \"AREA\") {\r\n"
			+ "circumference = 2 * 3.14 * radius;\r\n"
			+"}\r\n"
			+ "</cfscript>";
			
		cfBugs.process(scriptSrc, "test");

		scriptSrc = "<cfscript>\r\n"
			+ "area = 3.14 * radius * radius;\r\n"
			+ "volume = 4/3 * 3.14 * radius * radius * radius;\r\n"
			+ "</cfscript>";

		cfBugs.process(scriptSrc, "test");

		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
	}

}
