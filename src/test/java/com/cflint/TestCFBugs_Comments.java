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

public class TestCFBugs_Comments {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("ARG_DEFAULT_MISSING");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testVarAndArgs_Disabled() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING --->" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledSpacing() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---    CFLINT-DISABLE  ARG_DEFAULT_MISSING   --->" + "<cfargument name=\"xyz\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledCase() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---cflint-disable ARG_DEFAULT_MISSING   --->" + "<cfargument name=\"xyz\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_Disabled2() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING ,XXX --->" + "<cfargument name=\"xyz\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledOnParent() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING --->"
                + "<cffunction name=\"test\">\r\n" + "	" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledOnParent2() throws CFLintScanException {
        final String cfcSrc = "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING ---><cfcomponent>\r\n"
                + "<cffunction name=\"test\">\r\n" + "	" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledOther() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE SOMEOTHER--->" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_DEFAULT_MISSING", result.get(0).getMessageCode());
    }

    /* Entire function is commented */
    @Test
    public void testNestedComments() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<!---<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE SOMEOTHER--->" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "---></cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

}
