package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestDebugChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_ISDEBUGMODE");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testIsDebugModeInScript() throws CFLintScanException {
        final String scriptSrc = "<cfif IsDebugMode()> \n"
                + "    <cflog file=\"MyAppSilentTrace\" text=\"Page: #cgi.script_name#, \n"
                + "    completed query MyDBQuery; Query Execution time: \n"
                + "    #cfquery.ExecutionTime# Status: #Application.status#\"> \n" + "</cfif>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ISDEBUGMODE", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testIsDebugModeUpperCaseInScript() throws CFLintScanException {
        final String scriptSrc = "<cfif ISDEBUGMODE()> \n"
                + "    <cflog file=\"MyAppSilentTrace\" text=\"Page: #cgi.script_name#, \n"
                + "    completed query MyDBQuery; Query Execution time: \n"
                + "    #cfquery.ExecutionTime# Status: #Application.status#\"> \n" + "</cfif>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ISDEBUGMODE", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }
}
