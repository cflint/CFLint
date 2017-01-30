package com.cflint.config;

import java.util.Collection;
import java.util.HashSet;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.CFLintScanner;

public class CFLintChainedConfig implements CFLintConfiguration {

    final CFLintConfig config;
    final CFLintConfiguration parent;

    public CFLintChainedConfig(CFLintConfiguration config) {
        super();
        this.config = (CFLintConfig) config;
        parent = null;
    }

    public CFLintChainedConfig(CFLintConfiguration config, CFLintConfiguration parent) {
        super();
        this.config = (CFLintConfig) config;
        this.parent = parent;
    }

    /**
     * Create a nested configuration.
     * 
     * @param config
     * @return
     */
    public CFLintChainedConfig createNestedConfig(CFLintConfiguration config) {
        return config == null ? this : new CFLintChainedConfig(config, this);
    }

    @Override
    public boolean includes(PluginMessage pluginMessage) {
        return config.includes(pluginMessage)
                || (config.isInheritParent() && parent != null && parent.includes(pluginMessage));
    }

    @Override
    public boolean excludes(PluginMessage pluginMessage) {
        return config.excludes(pluginMessage)
                || (config.isInheritParent() && parent != null && parent.excludes(pluginMessage));
    }

    public CFLintConfiguration getParent() {
        return parent;
    }

    @Override
    public PluginInfoRule getRuleByClass(Class<?> clazz) {
        PluginInfoRule retval = config.getRuleByClass(clazz);
        if (retval != null || parent == null) {
            return retval;
        }
        return parent.getRuleByClass(clazz);
    }

    @Override
    public PluginInfoRule getRuleForPlugin(CFLintScanner plugin) {
        PluginInfoRule retval = config.getRuleForPlugin(plugin);
        if (retval != null || parent == null) {
            return retval;
        }
        return parent.getRuleForPlugin(plugin);
    }

    @Override
    public void addInclude(PluginMessage pluginMessage) {
        config.addInclude(pluginMessage);
    }

    @Override
    public void addExclude(PluginMessage pluginMessage) {
        config.addExclude(pluginMessage);
    }

    @Override
    public Collection<PluginInfoRule> getRules() {
        final HashSet<PluginInfoRule> activeRules = new HashSet<PluginInfoRule>();
        for (PluginInfoRule rule : getAllRules()) {
            for (PluginMessage pluginMessage : rule.getMessages()) {
                if (includes(pluginMessage) && !excludes(pluginMessage)) {
                    activeRules.add(rule);
                    break;
                }
            }
        }
        return activeRules;
    }

    public Collection<PluginInfoRule> getAllRules() {
        if (parent == null || !config.isInheritPlugins()) {
            return config.getRules();
        }
        final HashSet<PluginInfoRule> retval = new HashSet<PluginInfoRule>();
        if (parent instanceof CFLintChainedConfig) {
            retval.addAll(((CFLintChainedConfig) parent).getAllRules());
        } else {
            retval.addAll(parent.getRules());
        }
        // Override any rules from the parent configuration
        retval.addAll(config.getRules());
        return retval;

    }
}
