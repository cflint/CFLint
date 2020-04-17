package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_ComponentUse {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_COMPONENT");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testValidNamesTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "NicComponentNm.cfc");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(lintresult.getIssues().values().toString(), 0, result.size());
    }


}
