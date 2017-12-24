package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_ComponentName {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("USE_DISPLAY_NAME");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testDisplayNameTagBased() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent displayName=\"test\">\r\n</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testNameTagBased() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent name=\"test\">\r\n</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("USE_DISPLAY_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    // TODO
    public void testDisplayNameScriptBased() throws CFLintScanException {
        final String cfcSrc = "component displayName=\"test\" {}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(0, result.size());
    }

    // TODO
    public void testNameScriptBased() throws CFLintScanException {
        final String cfcSrc = "component name=\"test\" {}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("USE_DISPLAY_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

}
