package com.cflint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFExecuteChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_CFEXECUTE_TAG");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void test_BAD() throws CFLintScanException {

        final String cfcSrc = "<cfexecute name=\"date\" timeout=\"1\" variable=\"date\" />";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        assertEquals("Avoid leaving <cfexecute> tags in committed code. CFexecute can be used as an attack vector and is slow.",
                lintresult.getIssues().get("AVOID_USING_CFEXECUTE_TAG").get(0).getMessage());
    }

}
