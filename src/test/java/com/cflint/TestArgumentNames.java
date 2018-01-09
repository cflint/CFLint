package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestArgumentNames {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("ARGUMENT_MISSING_NAME","ARGUMENT_INVALID_NAME","ARGUMENT_ALLCAPS_NAME","ARGUMENT_TOO_SHORT","ARGUMENT_TOO_LONG","ARGUMENT_TOO_WORDY","ARGUMENT_IS_TEMPORARY","ARGUMENT_HAS_PREFIX_OR_POSTFIX");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testValidNamesTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"first_name\">\r\n" + "	<cfargument name=\"firstname\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testUpercaseNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"FIRSTNAME\">\r\n" + "	<cfargument name=\"LAST_NAME\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("ARGUMENT_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("ARGUMENT_ALLCAPS_NAME", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void invalidCharsInNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"$name\">\r\n" + "	<cfargument name=\"last$name\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("ARGUMENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("ARGUMENT_INVALID_NAME", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void nameTooShortTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"a\">\r\n" + "	<cfargument name=\"b\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("ARGUMENT_TOO_SHORT", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("ARGUMENT_TOO_SHORT", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void nameTooLongTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"isaveryveryverylongargumentname\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARGUMENT_TOO_LONG", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void nameTooWordyTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"nameIsFarTooWordy\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARGUMENT_TOO_WORDY", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void nameIsTemporyTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"temp\">\r\n" + "	<cfargument name=\"obj\">\r\n"
                + "	<cfargument name=\"struct\">\r\n" + "	<cfargument name=\"tempName\">\r\n"
                + "	<cfargument name=\"nameObj\">\r\n" + "	<cfargument name=\"nameString\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("ARGUMENT_IS_TEMPORARY");
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
    public void nameHasPrefixOrPostfixTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"sName\">\r\n" + "	<cfargument name=\"nameSt\">\r\n"
                + "	<cfargument name=\"oName\">\r\n" + "	<cfargument name=\"bOff\">\r\n"
                + "	<cfargument name=\"arrNames\">\r\n" + "	<cfargument name=\"thisName\">\r\n"
                + "	<cfargument name=\"myName\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
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
    public void testValidNamesScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test(firstName, first_name, firstname) {\r\n" + "}\r\n"
                + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testUpercaseNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test(FIRSTNAME, LAST_NAME) {\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("ARGUMENT_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
        assertEquals("ARGUMENT_ALLCAPS_NAME", result.get(1).getMessageCode());
        assertEquals(2, result.get(1).getLine());
    }

    @Test
    public void invalidCharsInNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test($name, last$name) {\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("ARGUMENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
        assertEquals("ARGUMENT_INVALID_NAME", result.get(1).getMessageCode());
        assertEquals(2, result.get(1).getLine());
    }


    @Test
    public void nameTooShortScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test(a, b) {\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("ARGUMENT_TOO_SHORT", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
        assertEquals("ARGUMENT_TOO_SHORT", result.get(1).getMessageCode());
        assertEquals(2, result.get(1).getLine());
    }

    @Test
    public void nameTooLongScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test(isaveryveryverylongvariablename) {\r\n" + "}\r\n"
                + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARGUMENT_TOO_LONG", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void nameTooWordyScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test(nameIsFarTooWordy, nameIsOK) {\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARGUMENT_TOO_WORDY", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void nameIsTemporyScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n"
                + "function test(temp, obj, struct, tempName, nameObj, nameString) {\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("ARGUMENT_IS_TEMPORARY");
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
    public void nameHasPrefixOrPostfixScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n"
                + "function test(aName, nameSt, oName, bOff, arrNames, thisName, myName) {\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
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
