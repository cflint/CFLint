package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.exception.CFLintScanException;
import com.cflint.plugins.core.ArgDefChecker;

public class TestCFBugs_Comments {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final CFLintConfig conf = new CFLintConfig();
        final PluginInfoRule pluginRule = new PluginInfoRule();
        pluginRule.setName("ArgDefChecker");
        conf.getRules().add(pluginRule);
        final PluginMessage pluginMessage = new PluginMessage("ARG_DEFAULT_MISSING");
        pluginMessage.setSeverity(Levels.WARNING);
        pluginMessage.setMessageText("Argument ${variable} is not required and does not define a default value.");
        pluginRule.getMessages().add(pluginMessage);

        cfBugs = new CFLint(conf, new ArgDefChecker());
    }

    @Test
    public void testVarAndArgs_Disabled() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING --->" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledSpacing() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---    CFLINT-DISABLE  ARG_DEFAULT_MISSING   --->" + "<cfargument name=\"xyz\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledCase() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---cflint-disable ARG_DEFAULT_MISSING   --->" + "<cfargument name=\"xyz\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_Disabled2() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING ,XXX --->" + "<cfargument name=\"xyz\">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledOnParent() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING --->"
                + "<cffunction name=\"test\">\r\n" + "	" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledOnParent2() throws CFLintScanException {
        final String cfcSrc = "<!---CFLINT-DISABLE ARG_DEFAULT_MISSING ---><cfcomponent>\r\n"
                + "<cffunction name=\"test\">\r\n" + "	" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_DisabledOther() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE SOMEOTHER--->" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_DEFAULT_MISSING", result.get(0).getMessageCode());
    }

    @Test
    public void testCFScript_QueryParams_Script_Hashes() throws CFLintScanException, IOException {
        final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("QueryParamChecker");
        cfBugs = new CFLint(conf);
    }

    @Test
    public void testCFScript_QueryParams_Script_HashesParent() throws CFLintScanException, IOException {
        final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("QueryParamChecker");
        cfBugs = new CFLint(conf);
    }

    /* Entire function is commented */
    @Test
    public void testNestedComments() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<!---<cffunction name=\"test\">\r\n" + "	"
                + "<!---CFLINT-DISABLE SOMEOTHER--->" + "<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n"
                + "---></cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        assertEquals(0, cfBugs.getBugs().getBugList().size());
    }

}
