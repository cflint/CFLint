package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;

public class TestCFBugsTagless {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws IOException, CFLintConfigurationException {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("MISSING_VAR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testSimpleCFSET() throws CFLintScanException {
        final String cfcSrc = "component accessors=true {\r\n" + "public name function init(){\r\n" + "return this;\r\n"
                + "}\r\n" + "\r\n" + "public void function test(){\r\n" + "myvar = \"test\";\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals(7, result.get(0).getLine());
        assertEquals("myvar", result.get(0).getVariable());
    }
}
