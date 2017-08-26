package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintConfiguration;
import com.cflint.exception.CFLintScanException;

public class TestAbortChecker {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final CFLintConfiguration conf = CFLintConfig.createDefaultLimited("AbortChecker");
        cfBugs = new CFLint(conf);
    }

    @Test
    public void test_no_abort() throws CFLintScanException {
        final String cfcSrc = "<cfscript>\r\n" + "a = 23;\r\n" + "</cfscript>";
        cfBugs.process(cfcSrc, "test");
        assertEquals(0, cfBugs.getBugs().getBugList().size());
    }

    @Test
    public void test_abort() throws CFLintScanException {
        final String cfcSrc = "<cfscript>\r\n" + "abort;\r\n" + "</cfscript>";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ABORT", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

}
