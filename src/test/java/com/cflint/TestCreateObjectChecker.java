package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCreateObjectChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_CREATEOBJECT");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testCreateObjectComponent() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var obj = createObject(\"component\",\"componentPath\");\r\n"
                + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_CREATEOBJECT", result.get(0).getMessageCode());
        assertEquals(12, result.get(0).getOffset());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testCreateObjectComponentMultiLine() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var obj = createObject(\r\n" + "\"component\",\r\n"
                + "\"componentPath\");\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_CREATEOBJECT", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testCreatObjectJava() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var a = 23;\r\n"
                + "var obj = createObject(\"java\",\"javaPath\");\r\n" + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

}
