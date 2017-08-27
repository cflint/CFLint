package com.cflint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestUnusedLocalVarChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("UNUSED_LOCAL_VARIABLE");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testAllUsedInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "component {\r\n" + "function someFunction() {\r\n"
                + "var a = 1;\r\n" + "var b = 2;\r\n" + "var c = 3;\r\n" + "return c * (a + b);\r\n" + "}\r\n" + "}\r\n"
                + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentFunctionInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "component {\r\n" + "function sum(a,b) {\r\n" + "return a + b;\r\n"
                + "}\r\n" + "}\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testUnusedVarInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "component {\r\n" + "function someFunction() {\r\n"
                + "var a = 1;\r\n" + "var b = 2;\r\n" + "var c = 2;\r\n" + "var sum = a + b;\r\n" + "return sum;\r\n"
                + "}\r\n" + "}\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
        assertEquals(6, result.get(0).getLine());
    }

    @Test
    public void testUnusedVarReturnInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "component {\r\n" + "function someFunction() {\r\n"
                + "var a = 1;\r\n" + "var b = 2;\r\n" + "var c = 2;\r\n" + "return a + b;\r\n" + "}\r\n" + "}\r\n"
                + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
        assertEquals(6, result.get(0).getLine());
    }

    @Test
    public void testUnusedVarFunctionInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "component {\r\n" + "function someFunction() {\r\n"
                + "var a = 1;\r\n" + "var b = 2;\r\n" + "var c = 2;\r\n" + "return otherFunc(a,b);\r\n" + "}\r\n"
                + "}\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
        assertEquals(6, result.get(0).getLine());
    }

    @Test
    public void testMultipleUnusedVarFunctionInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "component {\r\n" + "function someFunction() {\r\n"
                + "var a = 1;\r\n" + "var b = 2;\r\n" + "var c = 2;\r\n" + "var d = 2;\r\n" + "var e = 2;\r\n"
                + "return otherFunc(a,b);\r\n" + "}\r\n" + "}\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("UNUSED_LOCAL_VARIABLE");
        assertEquals(3, result.size());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
        assertEquals(6, result.get(0).getLine());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(1).getMessageCode());
        assertEquals(7, result.get(1).getLine());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(2).getMessageCode());
        assertEquals(8, result.get(2).getLine());
    }

    public void testAllUsedInTags() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction someFunction() {\r\n" + "<cfset var a = 1>\r\n"
                + "<cfset var b = 2>\r\n" + "<cfset var c = 3>\r\n" + "<cfreturn c * (a + b)>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentFunctionInTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"sum\">\r\n" + "<cfreturn a + b>\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testUnusedVarInTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"someFunction\">\r\n" + "<cfset var a = 1>\r\n"
                + "<cfset var b = 2>\r\n" + "<cfset var c = 2>\r\n" + "<cfset var sum = a + b>\r\n"
                + "<cfreturn sum>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
        assertEquals(5, result.get(0).getLine());
    }

    @Test
    public void testUnusedVarReturnInTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"someFunction\">\r\n" + "<cfset var a = 1>\r\n"
                + "<cfset var b = 2>\r\n" + "<cfset var c = 2>\r\n" + "<cfreturn a + b>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("UNUSED_LOCAL_VARIABLE");
        assertEquals(1, result.size());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
        assertEquals(5, result.get(0).getLine());
    }

    @Test
    public void testUnusedVarFunctionInTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"someFunction\">\r\n" + "<cfset var a = 1>\r\n"
                + "<cfset var b = 2>\r\n" + "<cfset var c = 2>\r\n" + "<cfreturn otherFunc(a,b)>\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("UNUSED_LOCAL_VARIABLE");
        assertEquals(1, result.size());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
        assertEquals(5, result.get(0).getLine());
    }

    @Test
    public void testMultipleUnusedVarFunctionInTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"someFunction\">\r\n" + "<cfset var a = 1>\r\n"
                + "<cfset var b = 2>\r\n" + "<cfset var c = 2>\r\n" + "<cfset var d = 3>\r\n" + "<cfset var e = 4>\r\n"
                + "<cfreturn otherFunc(a,b)>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("UNUSED_LOCAL_VARIABLE");
        assertEquals(3, result.size());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(0).getMessageCode());
        assertEquals(5, result.get(0).getLine());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(1).getMessageCode());
        assertEquals(6, result.get(1).getLine());
        assertEquals("UNUSED_LOCAL_VARIABLE", result.get(2).getMessageCode());
        assertEquals(7, result.get(2).getLine());
    }

    @Test
    public void testUsedVarInQuaryParam() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "<cffunction name=\"changePassword\">\r\n" + "<cfscript>\r\n"
                + "var salt = createUUID();\r\n" + "var hash = hash(password & salt);\r\n" + "var update = \"\";\r\n"
                + "</cfscript>\r\n" + "<cfquery name=\"update\">\r\n" + "update logins\r\n"
                + "set hash = <cfqueryparam value=\"#hash#\" cfsqltype=\"CF_SQL_VARCHAR\">\r\n" + "</cfquery>\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("UNUSED_LOCAL_VARIABLE");
        assertTrue(result == null);
    }

}
