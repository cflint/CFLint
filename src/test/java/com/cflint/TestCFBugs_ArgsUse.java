package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_ArgsUse {

    StackHandler handler = null;
    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("ARG_VAR_CONFLICT","ARG_VAR_MIXED");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testVarAndArgs() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset var xyz=123/>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_VAR_CONFLICT", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
    }

    @Test
    public void testVarAndArgs_Cfscript() throws CFLintScanException {
        final String cfcSrc = "component { \r\n" + "public void function foo(any arg1=\"\") { \r\n"
                + "var arg1=123; \r\n" + "} \r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_VAR_CONFLICT", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testVarAndArgs_No_OK() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset xyz=123/>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
    }

    @Test
    public void testVarAndArgs_Struct() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"page\" default=\"\">\r\n"
                + "	<cfset variables.instance.page = arguments.page />\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(lintresult.getIssues().toString(), 0, lintresult.getIssues().size());
    }

    @Test
    public void testVarAndArgs_MixedUse() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset xyz=123/>\r\n"
                + "	<cfset y=arguments.xyz/>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_VAR_CONFLICT", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
    }

    @Test
    public void testVarAndArgs_MixedUse_2x() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"\">\r\n" + "	<cfset xyz=123/>\r\n"
                + "	<cfset y=arguments.xyz/>\r\n" + "	<cfset z=arguments.xyz/>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_VAR_CONFLICT", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
    }
}
