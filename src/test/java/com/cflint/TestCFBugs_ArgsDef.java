package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_ArgsDef {

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

        cfBugs = new CFLint(conf);
    }

    @Test
    public void testVarAndArgs() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_DEFAULT_MISSING", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testVarAndArgs_Cfscript() throws CFLintScanException {
        final String cfcSrc = "component { \r\n" + "public void function foo(any arg1) { \r\n" + "} \r\n" + "}";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("ARG_DEFAULT_MISSING", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
    }

    @Test
    public void testVarAndArgs_OK1() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_OK2() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" required=\"true\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_Cfscript_OK() throws CFLintScanException {
        final String cfcSrc = "component { \r\n" + "public void function foo(required any arg1) { \r\n" + "} \r\n"
                + "}";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testVarAndArgs_Cfscript_OK2() throws CFLintScanException {
        final String cfcSrc = "component { \r\n" + "public void function foo(any arg1=\"123\") { \r\n" + "} \r\n" + "}";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

}
