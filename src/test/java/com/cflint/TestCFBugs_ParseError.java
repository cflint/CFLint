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

public class TestCFBugs_ParseError {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("PARSE_ERROR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testSimpleCFSET() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfset var x={}/>\r\n"
                + "	<cfset var x.y=123/>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Map<String, List<BugInfo>> result = lintresult.getIssues();

        assertEquals(0, result.size());
    }

    @Test
    public void testParseErrorLine1() throws CFLintScanException {
        final String cfcSrc = "<cfif \"foo\" ==== \"bar\">Foo</cfif>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().get("PARSE_ERROR");
        assertEquals(result.toString(), 1, result.size());
        assertEquals("PARSE_ERROR", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }
}
