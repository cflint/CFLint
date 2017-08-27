package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestBooleanExpressionChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("EXPLICIT_BOOLEAN_CHECK");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testIfBooleanExpressionInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "if (a && b == true) {\r\n" + "	c = 1;\r\n" + "}\r\n"
                + "else if (a or b is false) {\r\n" + "	c = 2;\r\n" + "} else {\r\n" + "	c = 3;\r\n" + "}\r\n"
                + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
        assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(1).getMessageCode());
        assertEquals(5, result.get(1).getLine());
    }

    @Test
    public void testSetBooleanExpressionInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "x = (a && b) == true;\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testIfBooleanExpressionInTag() throws CFLintScanException {
        final String tagSrc = "<cfif a and b is true>\r\n" + "<cfset c = 1>\r\n" + "<cfelseif a or b is false>\r\n"
                + "<cfset c = 2>\r\n" + "<cfelse>\r\n" + "<cfset c = 3>\r\n" + "</cfif>\r\n";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
        assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(1).getMessageCode());
        assertEquals(3, result.get(1).getLine());
    }

    @Test
    public void testSetBooleanExpressionInTag() throws CFLintScanException {
        final String tagSrc = "<cfset x = (a && b) == true>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());

    }

    @Test
    public void testReturnBooleanExpressionInScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "public function test() {\r\n" + "return (x && y) == true;\r\n"
                + "}\r\n" + "}\r\n";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testReturnBooleanExpressionInTag() throws CFLintScanException {
        final String tagSrc = "<cffunction name=\"test\">\r\n" + "<cfreturn (e and f) is true>\r\n" + "</cffunction>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("EXPLICIT_BOOLEAN_CHECK", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

}
