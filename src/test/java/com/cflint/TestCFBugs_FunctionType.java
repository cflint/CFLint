package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_FunctionType {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("FUNCTION_TYPE_MISSING","FUNCTION_TYPE_ANY");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testMissingReturnType() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>\r\n";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("FUNCTION_TYPE_MISSING", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testMissingReturnTypeNoTags() throws CFLintScanException {
        final String cfcSrc = "component {\r\n" + "public function test(arg1) {\r\n" + "}\r\n" + "}\r\n";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("FUNCTION_TYPE_MISSING", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testReturnTypeAny() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\" returnType=\"any\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\" type=\"any\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>\r\n";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("FUNCTION_TYPE_ANY", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testReturnTypeAnyNoTags() throws CFLintScanException {
        final String cfcSrc = "component {\r\n" + "public any function test(any arg1, numeric arg2) {\r\n" + "}\r\n"
                + "}\r\n";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("FUNCTION_TYPE_ANY", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

}
