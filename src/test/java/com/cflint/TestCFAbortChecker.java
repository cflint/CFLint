package com.cflint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFAbortChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_CFABORT_TAG");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void test_BAD() throws CFLintScanException {

        final String cfcSrc = "<cfabort>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        assertEquals("Avoid leaving <cfabort> tags in committed code.",
                lintresult.getIssues().get("AVOID_USING_CFABORT_TAG").get(0).getMessage());
    }

}
