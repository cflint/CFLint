package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestArrayNewChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_ARRAYNEW");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testArrayNewInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var a = 23;\r\n" + "var b = arrayNew(1);\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ARRAYNEW", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
        assertEquals(Levels.INFO, result.get(0).getSeverity());
        assertEquals("Use implict array construction instead (= []).", result.get(0).getMessage());
    }

    @Test
    public void testArrayNewMultiDimentionInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var a = 23;\r\n" + "var b = arrayNew(3);\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void testArrayNewInTag() throws CFLintScanException {
        final String tagSrc = "<cfset a = 23>\r\n" + "<cfset b = arrayNew(1)>";

        CFLintResult lintresult = cfBugs.scan(tagSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ARRAYNEW", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
        assertEquals(Levels.INFO, result.get(0).getSeverity());
        assertEquals("Use implict array construction instead (= []).", result.get(0).getMessage());
    }

}
