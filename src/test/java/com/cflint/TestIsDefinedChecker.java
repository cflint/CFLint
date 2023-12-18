package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestIsDefinedChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_ISDEFINED");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testIsDefinedInTag() throws CFLintScanException {
        final String scriptSrc = "<cfif IsDefined('#test#')>\n\n</cfif>";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ISDEFINED", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testIsDefinedUpperCaseInTag() throws CFLintScanException {
        final String scriptSrc = "<cfif ISDEFINED('#test#')>\n\n</cfif>";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ISDEFINED", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testIsDefinedInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>if( IsDefined('#test#') ) { \n\n }</cfscript>";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ISDEFINED", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testIsDefinedUpperCaseInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>if( IsDefined('#test#') ) { \n\n }</cfscript>";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ISDEFINED", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }
}
