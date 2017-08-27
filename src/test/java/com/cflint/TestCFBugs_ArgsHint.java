package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_ArgsHint {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("ARG_HINT_MISSING");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testMissingHint() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_HINT_MISSING", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testBlankHint() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\"  hint=\"\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_HINT_MISSING", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testHasHint() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\" hint=\"This is a test argument.\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

}
