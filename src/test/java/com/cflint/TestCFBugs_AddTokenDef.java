package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_AddTokenDef {

    private CFLintAPI cfBugs;

    
    /** 
     * @throws Exception Exception
     */
    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("ADDTOKEN_ATTR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testTokenDef() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent output=\"false\">\r\n" + "<cffunction name=\"test\" output=\"false\">\r\n"
                + "	<cflocation url=\"test\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ADDTOKEN_ATTR", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("<cflocation url=\"test\"> should have @addtoken='false'", result.get(0).getMessage());
    }

    @Test
    public void test_OK() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent output=\"false\">\r\n" + "<cffunction name=\"test\" output=\"false\">\r\n"
                + "	<cflocation url=\"\" addtoken=\"false\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

}
