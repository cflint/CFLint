package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.exception.CFLintScanException;
import com.cflint.plugins.core.VarScoper;

public class TestCFBugs_ParseError {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final CFLintConfig conf = new CFLintConfig();
        cfBugs = new CFLint(conf, new VarScoper());
        cfBugs.setLogError(true);
    }

    @Test
    public void testSimpleCFSET() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfset var x={}/>\r\n"
                + "	<cfset var x.y=123/>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        List<BugInfo> result = cfBugs.getBugs().getFlatBugList();

        assertEquals(0, result.size());
    }

    @Test
    public void testParseErrorLine1() throws CFLintScanException {
        final String cfcSrc = "<cfif \"foo\" ==== \"bar\">Foo</cfif>";
        cfBugs.process(cfcSrc, "test");
        List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
        assertEquals(result.toString(), 1, result.size());
        assertEquals("PARSE_ERROR", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }
}
