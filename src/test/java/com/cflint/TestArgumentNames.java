package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintConfiguration;

import cfml.parsing.reporting.ParseException;

public class TestArgumentNames {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final CFLintConfiguration conf = CFLintConfig.createDefaultLimited("ArgumentNameChecker");
		cfBugs = new CFLint(conf);

	}

	@Test
	public void testValidNamesTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"first_name\">\r\n"
		 + "	<cfargument name=\"firstname\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testUpercaseNameTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"FIRSTNAME\">\r\n"
		 + "	<cfargument name=\"LAST_NAME\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("ARGUMENT_ALLCAPS_NAME", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("ARGUMENT_ALLCAPS_NAME", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
	}

	@Test
	public void invalidCharsInNameTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"$name\">\r\n"
		 + "	<cfargument name=\"last$name\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("ARGUMENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("ARGUMENT_INVALID_NAME", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
	}

	@Test
	public void nameEndsInNumberTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"name_1\">\r\n"
		 + "	<cfargument name=\"name2\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("ARGUMENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("ARGUMENT_INVALID_NAME", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
	}

	@Test
	public void nameTooShortTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"a\">\r\n"
		 + "	<cfargument name=\"b\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("ARGUMENT_TOO_SHORT", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("ARGUMENT_TOO_SHORT", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
	}

	@Test
	public void nameTooLongTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"isaveryveryverylongargumentname\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARGUMENT_TOO_LONG", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
	}

	@Test
	public void nameTooWordyTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"nameIsFarTooWordy\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARGUMENT_TOO_WORDY", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
	}

	@Test
	public void nameIsTemporyTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"temp\">\r\n"
		 + "	<cfargument name=\"obj\">\r\n"
		 + "	<cfargument name=\"struct\">\r\n"
		 + "	<cfargument name=\"tempName\">\r\n" 
		 + "	<cfargument name=\"nameObj\">\r\n" 
		 + "	<cfargument name=\"nameString\">\r\n" 
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().get("ARGUMENT_IS_TEMPORARY");
		assertEquals(6, result.size());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(2).getMessageCode());
		assertEquals(5, result.get(2).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(3).getMessageCode());
		assertEquals(6, result.get(3).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(4).getMessageCode());
		assertEquals(7, result.get(4).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(5).getMessageCode());
		assertEquals(8, result.get(5).getLine());
	}

	@Test
	public void nameHasPrefixOrPostfixTag() throws ParseException, IOException {
		final String tagSrc = "<cfcomponent>\r\n"
		 + "<cffunction name=\"test\">\r\n"
		 + "	<cfargument name=\"sName\">\r\n"
		 + "	<cfargument name=\"nameSt\">\r\n"
		 + "	<cfargument name=\"oName\">\r\n"
		 + "	<cfargument name=\"bOff\">\r\n"
		 + "	<cfargument name=\"arrNames\">\r\n"
		 + "	<cfargument name=\"thisName\">\r\n"
		 + "	<cfargument name=\"myName\">\r\n"
		 + "</cffunction>\r\n"
		 + "</cfcomponent>";
		cfBugs.process(tagSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(7, result.size());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(1).getMessageCode());
		assertEquals(4, result.get(1).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(2).getMessageCode());
		assertEquals(5, result.get(2).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(3).getMessageCode());
		assertEquals(6, result.get(3).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(4).getMessageCode());
		assertEquals(7, result.get(4).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(5).getMessageCode());
		assertEquals(8, result.get(5).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(6).getMessageCode());
		assertEquals(9, result.get(6).getLine());
	}


	@Test
	public void testValidNamesScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test(firstName, first_name, firstname) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}

	@Test
	public void testUpercaseNameScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test(FIRSTNAME, LAST_NAME) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("ARGUMENT_ALLCAPS_NAME", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("ARGUMENT_ALLCAPS_NAME", result.get(1).getMessageCode());
		assertEquals(2, result.get(1).getLine());
	}

	@Test
	public void invalidCharsInNameScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test($name, last$name) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("ARGUMENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("ARGUMENT_INVALID_NAME", result.get(1).getMessageCode());
		assertEquals(2, result.get(1).getLine());
	}

	@Test
	public void nameEndsInNumberScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test(name_1, name2) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("ARGUMENT_INVALID_NAME", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("ARGUMENT_INVALID_NAME", result.get(1).getMessageCode());
		assertEquals(2, result.get(1).getLine());
	}

	@Test
	public void nameTooShortScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test(a, b) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(2, result.size());
		assertEquals("ARGUMENT_TOO_SHORT", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("ARGUMENT_TOO_SHORT", result.get(1).getMessageCode());
		assertEquals(2, result.get(1).getLine());
	}

	@Test
	public void nameTooLongScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test(isaveryveryverylongvariablename) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARGUMENT_TOO_LONG", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void nameTooWordyScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test(nameIsFarTooWordy, nameIsOK) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("ARGUMENT_TOO_WORDY", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
	}

	@Test
	public void nameIsTemporyScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test(temp, obj, struct, tempName, nameObj, nameString) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().get("ARGUMENT_IS_TEMPORARY");
		assertEquals(6, result.size());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(1).getMessageCode());
		assertEquals(2, result.get(1).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(2).getMessageCode());
		assertEquals(2, result.get(2).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(3).getMessageCode());
		assertEquals(2, result.get(3).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(4).getMessageCode());
		assertEquals(2, result.get(4).getLine());
		assertEquals("ARGUMENT_IS_TEMPORARY", result.get(5).getMessageCode());
		assertEquals(2, result.get(5).getLine());
	}

	@Test
	public void nameHasPrefixOrPostfixScript() throws ParseException, IOException {
		final String scriptSrc = "component {\r\n"
		 + "function test(aName, nameSt, oName, bOff, arrNames, thisName, myName) {\r\n"
		 + "}\r\n"
		 + "}";
		cfBugs.process(scriptSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(7, result.size());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
		assertEquals(2, result.get(0).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(1).getMessageCode());
		assertEquals(2, result.get(1).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(2).getMessageCode());
		assertEquals(2, result.get(2).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(3).getMessageCode());
		assertEquals(2, result.get(3).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(4).getMessageCode());
		assertEquals(2, result.get(4).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(5).getMessageCode());
		assertEquals(2, result.get(5).getLine());
		assertEquals("ARGUMENT_HAS_PREFIX_OR_POSTFIX", result.get(6).getMessageCode());
		assertEquals(2, result.get(6).getLine());
	}

}
