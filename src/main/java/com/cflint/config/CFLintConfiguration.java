package com.cflint.config;

import java.util.Collection;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.CFLintScanner;

public interface CFLintConfiguration {

    boolean includes(PluginMessage pluginMessage);

    boolean excludes(PluginMessage pluginMessage);

    PluginInfoRule getRuleByClass(Class<?> clazz);

    PluginInfoRule getRuleForPlugin(CFLintScanner plugin);

    void addInclude(PluginMessage pluginMessage);

    void addExclude(PluginMessage pluginMessage);

    Collection<CFLintPluginInfo.PluginInfoRule> getRules();
}
