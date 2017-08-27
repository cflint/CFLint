package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestParsingErrors {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("MISSING_VAR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testMissingSemiColon() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfscript>\r\n"
                + "   var xx = 123\r\n" + "   yy = 123;\r\n" + "	</cfscript>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");

        assertEquals(1, lintresult.getIssues().size());
        final List<BugInfo> result2 = lintresult.getIssues().get("MISSING_VAR");
        assertEquals(1, result2.size());
        assertEquals("MISSING_VAR", result2.get(0).getMessageCode());
        assertEquals("yy", result2.get(0).getVariable());
        assertEquals(5, result2.get(0).getLine());
        assertEquals(Levels.ERROR, result2.get(0).getSeverity());
        assertEquals("Variable yy is not declared with a var statement.", result2.get(0).getMessage());
    }
}
