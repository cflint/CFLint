package com.cflint.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Ignore;
import org.junit.Test;

import com.cflint.config.CFLintPluginInfo;
import com.cflint.config.ConfigUtils;

public class TestCFLintDoc {

    @Test
    @Ignore
    public void test(){
        final CFLintPluginInfo pluginInfo = ConfigUtils.loadDefaultPluginInfo();
        StringWriter out = new StringWriter();
        PrintWriter print = new PrintWriter(out);
        CFLintDoc.generateRuleMarkDown(pluginInfo, print);
        System.out.println(out);
    }
}
