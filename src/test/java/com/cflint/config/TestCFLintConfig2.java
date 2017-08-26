package com.cflint.config;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;

public class TestCFLintConfig2 {

    final String sconfig0 = "<config><includes code=\"ARG_VAR_CONFLICT\"/></config>";
    final String sconfig1 = "<config><includes code=\"COMPONENT_HINT_MISSING\"/></config>";
    final String sconfig2 = "<config><includes code=\"FUNCTION_HINT_MISSING\"/></config>";
    final String sconfig3 = "<config><includes code=\"ARG_HINT_MISSING\"/></config>";

    @Test
    public void test() throws Exception {
        CFLintConfiguration config = com.cflint.config.ConfigUtils.unmarshal(sconfig0, CFLintConfig.class);
        CFLintConfiguration config1 = com.cflint.config.ConfigUtils.unmarshal(sconfig1, CFLintConfig.class);

        assertTrue(config.includes(new PluginMessage("ARG_VAR_CONFLICT")));
        assertTrue(!config1.includes(new PluginMessage("ARG_VAR_CONFLICT")));
    }

}
