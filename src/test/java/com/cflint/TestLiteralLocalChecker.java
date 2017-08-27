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

public class TestLiteralLocalChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("LOCAL_LITERAL_VALUE_USED_TOO_OFTEN");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testOK() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "function calc() {\r\n" + "var pi = 3.14;\r\n"
                + "var code = \"CODE\";\r\n" + "}\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testTooManyHardCodedValues() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "function calc() {\r\n" + "var pi = 3.14;\r\n"
                + "var area = 3.14 * radius * radius;\r\n" + "var circumference = 2 * 3.14 * radius;\r\n"
                + "var volume = 4/3 * 3.14 * radius * radius * radius;\r\n" + "}\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("LOCAL_LITERAL_VALUE_USED_TOO_OFTEN", result.get(0).getMessageCode());
        assertEquals(6, result.get(0).getLine());
    }

}
