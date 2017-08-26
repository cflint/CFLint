package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.exception.CFLintScanException;

public class TestLiteralGlobalChecker {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("GlobalLiteralChecker",
                "LocalLiteralChecker");
        cfBugs = new CFLint(conf);
    }

    @Test
    public void testOK() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "pi = 3.14;\r\n" + "code = \"CODE\";\r\n" + "</cfscript>";

        cfBugs.process(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testTooManyGlobalHardCodedValues() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "area = 3.14 * radius * radius;\r\n"
                + "volume = 4/3 * 3.14 * radius * radius * radius;\r\n" + "var circumference = 2 * 3.14 * radius;\r\n"
                + "var volume = 4/3 * 3.14 * radius * radius * radius;\r\n" + "</cfscript>";

        cfBugs.process(scriptSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN", result.get(0).getMessageCode());
        assertEquals(5, result.get(0).getLine());
    }

    @Test
    public void testLocalNotGlobal() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "function calc() {\r\n" + "var area = 3.14 * radius * radius;\r\n"
                + "var volume = 4/3 * 3.14 * radius * radius * radius;\r\n"
                + "var circumference = 2 * 3.14 * radius;\r\n"
                + "var volume = 4/3 * 3.14 * radius * radius * radius;\r\n" + "}\r\n" + "</cfscript>";

        cfBugs.process(scriptSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("LOCAL_LITERAL_VALUE_USED_TOO_OFTEN", result.get(0).getMessageCode());
        assertEquals(6, result.get(0).getLine());
    }

}
