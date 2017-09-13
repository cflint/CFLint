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

public class TestCFBugs_SimpleComplexity {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("FUNCTION_TOO_COMPLEX");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testNotComplexTagBased() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"functionOne\">\r\n" + "<cfif a is 1>\r\n"
                + "<cfset b = 1>\r\n" + "<cfelse>\r\n" + "<cfset b = 2>\r\n" + "</cfif>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testComplexTagBased() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"functionTwo\">\r\n" + "<cfif a is 1>\r\n"
                + "<cfset b = 1>\r\n" + "<cfelseif a is 2>\r\n" + "<cfset b = 2>\r\n" + "<cfelseif a is 3>\r\n"
                + "<cfset b = 3>\r\n" + "<cfelseif a is 4>\r\n" + "<cfset b = 4>\r\n" + "<cfelseif a is 5>\r\n"
                + "<cfset b = 5>\r\n" + "<cfelseif a is 6>\r\n" + "<cfset b = 6>\r\n" + "<cfelseif a is 7>\r\n"
                + "<cfset b = 7>\r\n" + "<cfelseif a is 8>\r\n" + "<cfset b = 8>\r\n" + "<cfelseif a is 9>\r\n"
                + "<cfset b = 9>\r\n" + "<cfelseif a is 10>\r\n" + "<cfset b = 10>\r\n" + "<cfelseif a is 11>\r\n"
                + "<cfset b = 11>\r\n" + "</cfif>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FUNCTION_TOO_COMPLEX", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testNonComplexNameScriptBased() throws CFLintScanException {
        final String cfcSrc = "component {\r\n" + "function functionThree() {\r\n" + "if (a == 1) {" + "b = 1;\r\n"
                + "}\r\n" + "else {\r\n" + "b = 2;\r\n" + "}\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testIfStatementNoElse() throws CFLintScanException {
        final String cfcSrc = "component {\r\n" + "    public void function foo() {\r\n"
                + "        if (something) {\r\n" + "            doSomething();\r\n" + "        }\r\n" + "    }\r\n"
                + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testComplexScriptBased() throws CFLintScanException {
        final String cfcSrc = "component {\r\n" + "function functionFour() {\r\n" + "if (a == 1) {" + "b = 1;\r\n"
                + "}\r\n" + "else if (a == 2) {\r\n" + "b = 2;\r\n" + "}\r\n" + "else if (a == 3) {\r\n" + "b = 3;\r\n"
                + "}\r\n" + "else if (a == 4) {\r\n" + "b = 4;\r\n" + "}\r\n" + "else if (a == 5) {\r\n" + "b = 5;\r\n"
                + "}\r\n" + "else if (a == 6) {\r\n" + "b = 6;\r\n" + "}\r\n" + "else if (a == 7) {\r\n" + "b = 7;\r\n"
                + "}\r\n" + "else if (a == 8) {\r\n" + "b = 8;\r\n" + "}\r\n" + "else if (a == 9) {\r\n" + "b = 9;\r\n"
                + "}\r\n" + "else if (a == 10) {\r\n" + "b = 10;\r\n" + "else if (a == 11) {\r\n" + "b = 11;\r\n"
                + "}\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("FUNCTION_TOO_COMPLEX");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FUNCTION_TOO_COMPLEX", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testComplexSwitchScriptBased() throws CFLintScanException {
        final String cfcSrc = "component {\r\n" + "function functionFive() {\r\n" + "switch(a) {\r\n" + "case 1:\r\n"
                + "b = 1;\r\n" + "break;\r\n" + "case 2:\r\n" + "b = 2;\r\n" + "break;\r\n" + "case 3:\r\n"
                + "b = 3;\r\n" + "break;\r\n" + "case 4:\r\n" + "b = 4;\r\n" + "break;\r\n" + "case 5:\r\n"
                + "b = 5;\r\n" + "break;\r\n" + "case 6:\r\n" + "b = 6;\r\n" + "break;\r\n" + "case 7:\r\n"
                + "b = 7;\r\n" + "break;\r\n" + "case 8:\r\n" + "b = 8;\r\n" + "break;\r\n" + "case 9:\r\n"
                + "b = 9;\r\n" + "break;\r\n" + "case 10:\r\n" + "b = 10;\r\n" + "break;\r\n" + "case 11:\r\n"
                + "b = 11;\r\n" + "break;\r\n" + "}\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("FUNCTION_TOO_COMPLEX");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cfcSrc.indexOf("function"),result.get(0).getOffset());
    }

}
