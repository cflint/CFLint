package com.cflint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestIgnoreLine {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder();
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testIgnoreLines() throws CFLintScanException {
        final String scriptSrc = "component { // cflint ignore:line\n" + 
                "    function functionFour() { // cflint ignore:line\n" + 
                "        if (a == 1) { // cflint ignore:line\n" + 
                "            b =  // cflint ignore:line\n" + 
                "        }\n" + 
                "    }\n" + 
                "}";
        cfBugs.setLogError(true);
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

}
