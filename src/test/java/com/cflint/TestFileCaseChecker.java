package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.exception.CFLintScanException;

public class TestFileCaseChecker {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("FileCaseChecker");
        cfBugs = new CFLint(conf);
    }

    @Test
    public void testLowerCaseCFMName() throws CFLintScanException {
        final String cfmSrc = "<cfset a = 23>";
        cfBugs.process(cfmSrc, "test.cfm");
        Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testLowerCaseCFCName() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent></cfcomponent>";
        cfBugs.process(cfcSrc, "test.cfc");
        Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testUppeCaseCFMName() throws CFLintScanException {
        final String cfmSrc = "<cfset a = 23>";
        cfBugs.process(cfmSrc, "Test.cfm");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("FILE_SHOULD_START_WITH_LOWERCASE", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testUppperCaseCFCName() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent></cfcomponent>";
        cfBugs.process(cfcSrc, "Test.cfc");
        Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
        assertEquals(0, result.size());
    }

}
