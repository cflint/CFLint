package com.cflint.config;

import java.util.Collection;
import java.util.HashSet;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.CFLintScanner;

public class CFLintChainedConfig extends BaseConfig{

    private final CFLintConfig config;
    private final CFLintConfiguration parent;

    public CFLintChainedConfig(final CFLintConfiguration config) {
        super();
        this.config = (CFLintConfig) config;
        parent = null;
    }

    public CFLintChainedConfig(final CFLintConfiguration config, final CFLintConfiguration parent) {
        super();
        this.config = (CFLintConfig) config;
        this.parent = parent;
    }

    /**
     * Create a nested configuration.
     * 
     * @param config    Configuration to merge
     * @return          Combined configuration
     */
    public CFLintChainedConfig createNestedConfig(final CFLintConfiguration config) {
        return config == null ? this : new CFLintChainedConfig(config, this);
    }

    @Override
    public boolean includes(final PluginMessage pluginMessage) {
        return config.includes(pluginMessage)
                || (config.isInheritParent() && parent != null && parent.includes(pluginMessage));
    }

    @Override
    public boolean excludes(final PluginMessage pluginMessage) {
        return config.excludes(pluginMessage)
                || (config.isInheritParent() && parent != null && parent.excludes(pluginMessage));
    }

    /**
     * 
     * @return the parent configuration
     */
    public CFLintConfiguration getParent() {
        return parent;
    }

    @Override
    public PluginInfoRule getRuleByClass(final Class<?> clazz) {
        PluginInfoRule retval = config.getRuleByClass(clazz);
        if (retval != null || parent == null) {
            return retval;
        }
        return parent.getRuleByClass(clazz);
    }

    @Override
    public PluginInfoRule getRuleForPlugin(final CFLintScanner plugin) {
        PluginInfoRule retval = config.getRuleForPlugin(plugin);
        if (retval != null || parent == null) {
            return retval;
        }
        return parent.getRuleForPlugin(plugin);
    }

    @Override
    public void addInclude(final PluginMessage pluginMessage) {
        config.addInclude(pluginMessage);
    }

    @Override
    public void addExclude(final PluginMessage pluginMessage) {
        config.addExclude(pluginMessage);
    }

    @Override
    public Collection<PluginInfoRule> getRules() {
        final HashSet<PluginInfoRule> activeRules = new HashSet<>();
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

    /**
     * List all the rules.
     * @return the list of rules
     */
    public Collection<PluginInfoRule> getAllRules() {
        if (parent == null) {
            return config.getRules();
        }
        final HashSet<PluginInfoRule> retval = new HashSet<>();
        if (parent instanceof CFLintChainedConfig) {
            retval.addAll(((CFLintChainedConfig) parent).getAllRules());
        } else {
            retval.addAll(parent.getRules());
        }
        // Override any rules from the parent configuration
        retval.addAll(config.getRules());
        return retval;

    }
    
    @Override
    public Object getParameter(final String name) {
        final Object retval = config.getParameter(name);
        if(retval !=null || parent == null){
            return retval;
        }
        return parent.getParameter(name);
    }
}
