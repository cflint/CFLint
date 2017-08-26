package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.exception.CFLintScanException;
import com.cflint.plugins.core.VarScoper;

public class TestParsingErrors {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        CFLintConfig conf = new CFLintConfig();
        PluginInfoRule pluginRule = new PluginInfoRule();
        pluginRule.setName("VarScoper");
        conf.getRules().add(pluginRule);
        PluginMessage pluginMessage = new PluginMessage("MISSING_VAR");
        pluginMessage.setSeverity(Levels.ERROR);
        pluginMessage.setMessageText("Variable ${variable} is not declared with a var statement.");
        pluginRule.getMessages().add(pluginMessage);

        cfBugs = new CFLint(conf, new VarScoper());
    }

    @Test
    public void testMissingSemiColon() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfscript>\r\n"
                + "   var xx = 123\r\n" + "   yy = 123;\r\n" + "	</cfscript>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");

        assertEquals(1, cfBugs.getBugs().getFlatBugList().size());
        final List<BugInfo> result2 = cfBugs.getBugs().getBugList().get("MISSING_VAR");
        assertEquals(1, result2.size());
        assertEquals("MISSING_VAR", result2.get(0).getMessageCode());
        assertEquals("yy", result2.get(0).getVariable());
        assertEquals(5, result2.get(0).getLine());
        assertEquals(Levels.ERROR, result2.get(0).getSeverity());
        assertEquals("Variable yy is not declared with a var statement.", result2.get(0).getMessage());
    }
}
