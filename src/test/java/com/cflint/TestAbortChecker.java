package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestAbortChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_ABORT");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void test_no_abort() throws CFLintScanException {
        final String cfcSrc = "<cfscript>\r\n" + "a = 23;\r\n" + "</cfscript>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void test_abort() throws CFLintScanException {
        final String cfcSrc = "<cfscript>\r\n" + "abort;\r\n" + "</cfscript>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ABORT", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

}
