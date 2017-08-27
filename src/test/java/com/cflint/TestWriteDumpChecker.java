package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestWriteDumpChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_WRITEDUMP");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testWriteDumpInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var a = 23;\r\n" + "writeDump(a);\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_WRITEDUMP", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testWriteDumpMixedCaseScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var a = 23;\r\n" + "WriteDUMP(a);\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_WRITEDUMP", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testWriteDumpInTag() throws CFLintScanException {
        final String tagSrc = "<cfset a = 23>\r\n" + "<cfset writeDump(a)>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_WRITEDUMP", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

}
