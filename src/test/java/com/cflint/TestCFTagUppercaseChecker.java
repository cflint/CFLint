package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.exception.CFLintScanException;
import com.cflint.plugins.core.CFTagCaseChecker;

public class TestCFTagUppercaseChecker {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final CFLintConfig conf = new CFLintConfig();
        PluginInfoRule pluginRule = new PluginInfoRule();
        pluginRule.setName("CFTagCaseChecker");
        conf.getRules().add(pluginRule);
        PluginMessage pluginMessage = new PluginMessage("CFTAG_PREFERRED_CASE");
        pluginMessage.setSeverity(Levels.ERROR);
        pluginMessage.setMessageText(
                "Tag <${variable}> should be written in lowercase or camelCase for consistency in code.");
        pluginRule.getMessages().add(pluginMessage);

        CFTagCaseChecker caseChecker = new CFTagCaseChecker();
        caseChecker.setParameter("PreferCase", "upper");
        cfBugs = new CFLint(conf, caseChecker);
        cfBugs.setLogError(false);
    }

    @Test
    public void testUpperCase_BAD() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("CFTAG_PREFERRED_CASE", result.get(0).getMessageCode());
    }

    @Test
    public void testUpperCase_GOOD() throws CFLintScanException {
        final String cfcSrc = "<CFCOMPONENT>\r\n" + "<cffunction name='lowercase'>\r\n" + "</cffunction>\r\n"
                + "</CFCOMPONENT>";
        cfBugs.process(cfcSrc, "test");
        List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
    }
}
