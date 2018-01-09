package com.cflint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_VariableNames {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("VAR_INVALID_NAME","VAR_ALLCAPS_NAME","SCOPE_ALLCAPS_NAME","VAR_TOO_SHORT","VAR_TOO_LONG","VAR_TOO_WORDY","VAR_IS_TEMPORARY","VAR_HAS_PREFIX_OR_POSTFIX");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testValidNamesTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset var firstName = \"Fred\">\r\n" + "	<cfset first_name = \"Fred\">\r\n"
                + "	<cfset firstname = \"Fred\">\r\n" + "	<cfset name.first = \"Fred\">\r\n"
                + "	<cfset name.last = \"Smith\">\r\n" + "	<cfset names[1] = \"Smith\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();

        assertEquals(0, result.size());
    }

    @Test
    public void testUpercaseNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset var FIRSTNAME = \"Fred\">\r\n" + "	<cfset LAST_NAME = \"Smith\">\r\n"
                + "	<cfset names = {}>\r\n" + "	<cfset name.FIRST = \"Fred\">\r\n"
                + "	<cfset NAMES[1] = \"Fred\">\r\n" + "	<cfset local.NAME = \"Fred\">\r\n"
                + "	<cfset variables.name = \"Fred\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals("VAR_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(8, result.get(0).getLine());
    }

    @Test
    public void testUpercaseScopeTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset local.name = \"Fred\">\r\n" + "	<cfset THIS.name = \"Fred\">\r\n"
                + "	<cfset variables.name = \"Fred\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("SCOPE_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
    }

    @Test
    public void invalidCharsInNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset var $name = \"Fred\">\r\n" + "	<cfset last$name = \"Smith\">\r\n"
                + "	<cfset last.$name = \"Smith\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("VAR_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_INVALID_NAME", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
        assertEquals("last$name", result.get(1).getVariable());
        assertEquals(tagSrc.indexOf("last$name"), result.get(1).getOffset());
    }

    @Test
    public void nameTooShortTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset a = \"Fred\">\r\n" + "	<cfset b = \"Smith\">\r\n" + "	<cfset last.a = \"Fred\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("VAR_TOO_SHORT", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_TOO_SHORT", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void nameTooLongTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset isaveryveryverylongvariablename = \"Fred\">\r\n"
                + "	<cfset isa.veryveryverylongvariablename = \"Fred\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("VAR_TOO_LONG", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_TOO_LONG", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void nameTooWordyTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset nameIsFarTooWordy = \"Fred\">\r\n" + "	<cfset nameIsOK = \"Fred\">\r\n"
                + "	<cfset name.isAlsoFarTooWordy = \"Fred\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("VAR_TOO_WORDY", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_TOO_WORDY", result.get(1).getMessageCode());
        assertEquals(5, result.get(1).getLine());
    }

    @Test
    public void nameIsTemporyTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset temp = \"Fred\">\r\n" + "	<cfset name.temp = \"Fred\">\r\n"
                + "	<cfset obj = \"Fred\">\r\n" + "	<cfset struct = \"Fred\">\r\n"
                + "	<cfset tempName = \"Fred\">\r\n" + "	<cfset nameObj = \"Fred\">\r\n"
                + "	<cfset nameString = \"Fred\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("VAR_IS_TEMPORARY");
        assertEquals(6, result.size());
        assertEquals("VAR_IS_TEMPORARY", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(1).getMessageCode());
        assertEquals(5, result.get(1).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(2).getMessageCode());
        assertEquals(6, result.get(2).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(3).getMessageCode());
        assertEquals(7, result.get(3).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(4).getMessageCode());
        assertEquals(8, result.get(4).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(5).getMessageCode());
        assertEquals(9, result.get(5).getLine());
    }

    @Test
    public void nameHasPrefixOrPostfixTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset sName = \"Fred\">\r\n" + "	<cfset nameSt = {first:\"Fred\"}>\r\n"
                + "	<cfset oName = {first:\"Fred\"}>\r\n" + "	<cfset bOff = true>\r\n"
                + "	<cfset arrNames = [\"Fred\"]>\r\n" + "	<cfset thisName = \"Fred\">\r\n"
                + "	<cfset myName = \"Fred\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(7, result.size());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(2).getMessageCode());
        assertEquals(5, result.get(2).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(3).getMessageCode());
        assertEquals(6, result.get(3).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(4).getMessageCode());
        assertEquals(7, result.get(4).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(5).getMessageCode());
        assertEquals(8, result.get(5).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(6).getMessageCode());
        assertEquals(9, result.get(6).getLine());
    }

    @Test
    public void QueryValidNameTag() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
                + "<cfquery name=\"Categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
                + "WHERE p.id = <cfqueryparam value=\"#productid#\"/>\r\n"
                + "and p.name = <cfqueryparam value=\"abc\"/>\r\n" + "</cfquery>\r\n" + "</cffunction>"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("VAR_INVALID_NAME");
        assertEquals(result.toString(), 1, result.size());
        assertEquals("VAR_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testValidNamesScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n" + "	var firstName = \"Fred\";\r\n"
                + "	first_name = \"Fred\";\r\n" + "	firstname = \"Fred\";\r\n" + "	name.first = \"Fred\";\r\n"
                + "	name.last = \"Smith\";\r\n" + "	names[1] = \"Smith\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testUpercaseNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n" + "	var FIRSTNAME = \"Fred\";\r\n"
                + "	LAST_NAME = \"Smith\";\r\n" + "	names = {};\r\n" + "	name.FIRST = \"Fred\";\r\n"
                + "	NAMES[1] = \"Fred\";\r\n" + "	local.NAME = \"Fred\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("VAR_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(8, result.get(0).getLine());
    }

    @Test
    public void testUpercaseScopeScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n" + "	this.name = \"Fred\";\r\n"
                + "	LOCAL.name = \"Fred\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("SCOPE_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
    }

    @Test
    public void invalidCharsInNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n" + "	var $name = \"Fred\";\r\n"
                + "	last$name = \"Smith\";\r\n" + "	last.$name = \"Smith\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("VAR_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_INVALID_NAME", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void nameTooShortScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n" + "	a = \"Fred\";\r\n"
                + "	b = \"Smith\";\r\n" + "	last.a = \"Fred\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("VAR_TOO_SHORT", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_TOO_SHORT", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void nameTooLongScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n"
                + "	isaveryveryverylongvariablename = \"Fred\";\r\n"
                + "	isa.veryveryverylongvariablename = \"Fred\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("VAR_TOO_LONG", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_TOO_LONG", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void nameTooWordyScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n"
                + "	nameIsFarTooWordy = \"Fred\";\r\n" + "	nameIsOK = \"Fred\";\r\n"
                + "	name.isAlsoFarTooWordy = \"Fred\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("VAR_TOO_WORDY", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_TOO_WORDY", result.get(1).getMessageCode());
        assertEquals(5, result.get(1).getLine());
    }

    @Test
    public void nameIsTemporyScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n" + "	temp = \"Fred\";\r\n"
                + "	name.temp = \"Fred\";\r\n" + "	obj = \"Fred\";\r\n" + "	struct = \"Fred\";\r\n"
                + "	tempName = \"Fred\";\r\n" + "	nameObj = \"Fred\";\r\n" + "	nameString = \"Fred\";\r\n"
                + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("VAR_IS_TEMPORARY");
        assertEquals(6, result.size());
        assertEquals("VAR_IS_TEMPORARY", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(1).getMessageCode());
        assertEquals(5, result.get(1).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(2).getMessageCode());
        assertEquals(6, result.get(2).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(3).getMessageCode());
        assertEquals(7, result.get(3).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(4).getMessageCode());
        assertEquals(8, result.get(4).getLine());
        assertEquals("VAR_IS_TEMPORARY", result.get(5).getMessageCode());
        assertEquals(9, result.get(5).getLine());
    }

    @Test
    public void nameIsTemporyNestedScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function foo() {\r\n" + "    var tempStruct = {};\r\n"
                + "    doSomething(tempStruct);\r\n" + "    tempStruct.foo = \"bar\";\r\n" + "  }\r\n" + "}\r\n";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("VAR_IS_TEMPORARY");
        assertEquals(1, result.size());
    }

    @Test
    public void nameHasPrefixOrPostfixScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "function test() {\r\n" + "	sName = \"Fred\";\r\n"
                + "	nameSt = {first:\"Fred\"};\r\n" + "	oName = {first:\"Fred\"};\r\n"
                + "	bOff = true;\r\n" + "	arrNames = [\"Fred\"];\r\n" + "	thisName = \"Fred\";\r\n"
                + "	myName = \"Fred\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(7, result.size());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(2).getMessageCode());
        assertEquals(5, result.get(2).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(3).getMessageCode());
        assertEquals(6, result.get(3).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(4).getMessageCode());
        assertEquals(7, result.get(4).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(5).getMessageCode());
        assertEquals(8, result.get(5).getLine());
        assertEquals("VAR_HAS_PREFIX_OR_POSTFIX", result.get(6).getMessageCode());
        assertEquals(9, result.get(6).getLine());
    }

}
