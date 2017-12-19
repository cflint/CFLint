package com.cflint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFDebugAttributeTagChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_DEBUG_ATTR","AVOID_USING_CFSETTING_DEBUG");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testDebugNoVal() throws CFLintScanException {
        final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" debug> \n"
                + "    SELECT * FROM TestTable \n" + "</cfquery>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().get("AVOID_USING_DEBUG_ATTR").size());
    }

    @Test
    public void testDebugNoValCASE() throws CFLintScanException {
        final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" DEBUG> \n"
                + "    SELECT * FROM TestTable \n" + "</cfquery>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().get("AVOID_USING_DEBUG_ATTR").size());
    }

    @Test
    public void testCfsettingDebugNo() throws CFLintScanException {
        final String cfcSrc = "<cfsetting showDebugOutput=\"No\">";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void testCfsettingDebugYes() throws CFLintScanException {
        final String cfcSrc = "<cfsetting showDebugOutput=\"Yes\">";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().get("AVOID_USING_CFSETTING_DEBUG").size());
    }

    @Test
    public void testCfsettingNoAttributes() throws CFLintScanException {
        final String cfcSrc = "<cfset var a = 1>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }
}
