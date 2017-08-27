package com.cflint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo;
import com.cflint.config.ConfigBuilder;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.exception.CFLintScanException;

public class TestCFCustomTagChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        
        final CFLintConfig conf = new CFLintConfig();
        final PluginInfoRule pluginRuleX = new PluginInfoRule();
        pluginRuleX.setName("CFXTagChecker");
        conf.getRules().add(pluginRuleX);
        final PluginMessage pluginMessageX = new PluginMessage("AVOID_USING_MYLIB:CFCUSTOMTAG_TAG");
        pluginMessageX.setSeverity(Levels.WARNING);
        pluginMessageX.setMessageText(
                "Avoid Leaving <${tagName}> tags in committed code. Debug information should be ommited from release code");
        pluginRuleX.getMessages().add(pluginMessageX);
        pluginRuleX.addParameter("tagName", ".*:cfcustomtag");
        
        CFLintPluginInfo pluginInfo = new CFLintPluginInfo();
        pluginInfo.getRules().add(pluginRuleX);
        pluginRuleX.setClassName("CFXTagChecker");
        cfBugs = new CFLintAPI(new ConfigBuilder(pluginInfo).include("AVOID_USING_MYLIB:CFCUSTOMTAG_TAG").build());
        cfBugs.setLogError(false);
    }

    @Test
    public void test_BAD() throws CFLintScanException {
        final String cfcSrc = "<mylib:cfcustomtag x=1/>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
    }

    @Test
    public void test_GOOD() throws CFLintScanException {
        final String cfcSrc = "<customtag>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }
}
