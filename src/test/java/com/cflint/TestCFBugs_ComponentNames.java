package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.ComponentNameChecker;

public class TestCFBugs_ComponentNames {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("ComponentNameChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("COMPONENT_INVALID_NAME");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("COMPONENT_ALLCAPS_NAME");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("COMPONENT_TOO_SHORT");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginRule.addParameter("MinLength", "3");
		pluginRule.addParameter("MaxLength", "15");
		pluginMessage = new PluginMessage("COMPONENT_TOO_LONG");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("COMPONENT_TOO_WORDY");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginRule.addParameter("MaxWords", "3");
		pluginMessage = new PluginMessage("COMPONENT_IS_TEMPORARY");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);
		pluginMessage = new PluginMessage("COMPONENT_HAS_PREFIX_OR_POSTFIX");
		pluginMessage.setSeverity("INFO");
		pluginRule.getMessages().add(pluginMessage);

		final ComponentNameChecker checker = new ComponentNameChecker();
		cfBugs = new CFLint(conf, checker);
		cfBugs.setVerbose(true);
	}

	@Test
	public void testValidNamesTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "NiceComponentName.cfc");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testUpercaseNameTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "UPPERCASE.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_ALLCAPS_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void testLowercaseNameTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "lowercase.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void invalidCharsInNameTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "Last$name.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameEndsInNumberTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "Component23.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameTooShortTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "A.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_TOO_SHORT", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameTooLongTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "Acomponentwithaveryverylongcomponentname.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_TOO_LONG", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameTooWordyTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "NameIsTooWordy.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_TOO_WORDY", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameIsTemporyTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "Temp.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_IS_TEMPORARY", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameHasPrefixOrPostfixTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "MyComp.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}


	@Test
	public void testValidNamesScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "NiceComponentName.cfc");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testLowercaseNameScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "lowercase.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void testUpercaseNameScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "UPPERCASE.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_ALLCAPS_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void invalidCharsInNameScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "Component$Name.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameEndsInNumberScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "Component23.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameTooShortScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "A.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_TOO_SHORT", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameTooLongScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "Componenthasaverylongcomponentname.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_TOO_LONG", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameTooWordyScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "NameIsTooWordy.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_TOO_WORDY", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameIsTemporyScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "Temp.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_IS_TEMPORARY", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

	@Test
	public void nameHasPrefixOrPostfixScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "ThisComponent.cfc");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("COMPONENT_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
	}

}
