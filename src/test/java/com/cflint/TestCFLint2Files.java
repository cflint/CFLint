package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFLint2Files {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("ARG_DEFAULT_MISSING","QUERYPARAM_REQ");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testVarAndArgs_DisabledOther() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE SOMEOTHER--->" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_DEFAULT_MISSING", result.get(0).getMessageCode());

        final String cfcSrc2 = "component {\n" + "    public string function fooFunction() {\n"
                + "        return foo = bar\n" + "    }\n" + "}";
        lintresult = cfBugs.scan(cfcSrc2, "test");
        final List<BugInfo> result2 = lintresult.getIssues().get("MISSING_SEMI");
        assertEquals(null, result2);
    }
}
