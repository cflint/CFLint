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

public class TestFileCaseChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("FILE_SHOULD_START_WITH_LOWERCASE");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testLowerCaseCFMName() throws CFLintScanException {
        final String cfmSrc = "<cfset a = 23>";
        CFLintResult lintresult = cfBugs.scan(cfmSrc, "test.cfm");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testLowerCaseCFCName() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent></cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test.cfc");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testUppeCaseCFMName() throws CFLintScanException {
        final String cfmSrc = "<cfset a = 23>";
        CFLintResult lintresult = cfBugs.scan(cfmSrc, "Test.cfm");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("FILE_SHOULD_START_WITH_LOWERCASE", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testUppperCaseCFCName() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent></cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "Test.cfc");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

}
