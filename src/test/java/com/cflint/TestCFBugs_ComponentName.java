package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_ComponentName {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final com.cflint.config.CFLintConfiguration conf = CFLintConfig
                .createDefaultLimited("ComponentDisplayNameChecker");
        cfBugs = new CFLint(conf);
    }

    @Test
    public void testDisplayNameTagBased() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent displayName=\"test\">\r\n</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testNameTagBased() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent name=\"test\">\r\n</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("USE_DISPLAY_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    // TODO
    public void testDisplayNameScriptBased() throws CFLintScanException {
        final String cfcSrc = "component displayName=\"test\" {}";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(0, result.size());
    }

    // TODO
    public void testNameScriptBased() throws CFLintScanException {
        final String cfcSrc = "component name=\"test\" {}";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("USE_DISPLAY_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

}
