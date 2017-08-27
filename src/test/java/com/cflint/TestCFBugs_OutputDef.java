package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_OutputDef {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("OUTPUT_ATTR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testFunctionDef() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent output=\"false\">\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("OUTPUT_ATTR", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
        assertEquals("<cffunction name=\"test\"> should have @output='false'", result.get(0).getMessage());
    }

    @Test
    public void test_OK() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent output=\"false\">\r\n" + "<cffunction name=\"test\" output=\"false\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

}
