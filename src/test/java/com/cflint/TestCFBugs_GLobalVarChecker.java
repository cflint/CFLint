package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_GLobalVarChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("GLOBAL_VAR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testApplication() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset application.bf=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
        assertEquals("application", result.get(0).getVariable());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testApplication_1x() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset application.bf=\"123\">\r\n" + "	<cfset application.another=\"123\">\r\n"
                + "	<cfset form.id=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
        assertEquals("application", result.get(0).getVariable());
        assertEquals(3, result.get(0).getLine());
        assertEquals("GLOBAL_VAR", result.get(1).getMessageCode());
        assertEquals("form", result.get(1).getVariable());
        assertEquals(5, result.get(1).getLine());
    }

    @Test
    public void testCGI() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset CGI.bf=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
        assertEquals("CGI", result.get(0).getVariable());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testHashNestedCGI() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset a.books = #CGI.user.books#>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
        assertEquals("CGI", result.get(0).getVariable());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testHashLiteralNestedCGI() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfset a.books = '#CGI.user.books#'>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
        assertEquals("CGI", result.get(0).getVariable());
        assertEquals(3, result.get(0).getLine());
    }

}
