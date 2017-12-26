package com.cflint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFDumpChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_CFDUMP_TAG");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void test_BAD() throws CFLintScanException {

        final String cfcSrc = "<CFDUMP " + " var = \"#variable#\">";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        assertEquals(
                "Avoid leaving <cfdump> tags in committed code. Debug information should be omitted from release code",
                lintresult.getIssues().get("AVOID_USING_CFDUMP_TAG").get(0).getMessage());
    }

}
