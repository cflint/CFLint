package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestPointlessChecker {

    private CFLintAPI cfBugs;

    
    /** 
     * @throws Exception Exception
     */
    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("POINTLESS_CHECK");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testPointlessIfStatementInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "if (a);\r\n"
                + "</cfscript>";

        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("POINTLESS_CHECK", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

}
