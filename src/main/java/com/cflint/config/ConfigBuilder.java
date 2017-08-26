package com.cflint.config;

import java.util.List;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintPluginInfo.RuleGroup;

public class ConfigBuilder {

    private final CFLintPluginInfo pluginInfo;
    private final CFLintConfig defaultConfig;
    private CFLintConfig configFileConfig = null;
    private CFLintConfig cmdLineConfig = null;

    public ConfigBuilder() {
        this(ConfigUtils.loadDefaultPluginInfo());
    }

    public ConfigBuilder(final CFLintPluginInfo pluginInfo) {
        this.pluginInfo = pluginInfo;
        this.defaultConfig = new CFLintConfig();
        this.defaultConfig.setRules(pluginInfo.getRules());
    }

    /**
     * Apply the listed rule groups
     *
     * @param main
     * @param pluginInfo
     * @param rulegroups
     */
    public void ruleGroups(final String rulegroups) {
        final boolean include = !rulegroups.startsWith("!");
        for (final RuleGroup rg : pluginInfo.getRuleGroups()) {
            if (rulegroups.contains(rg.getName())) {
                if (include) {
                    defaultConfig.getIncludes().addAll(rg.getMessages());
                } else {
                    defaultConfig.getExcludes().addAll(rg.getMessages());
                }
            }
        }
    }

    /**
     * Add a custom configuration on top of the baseline. Hint: most
     * configurations do not need this.
     * 
     * @param fileName
     */
    public void addCustomConfig(final String fileName) throws Exception {
        configFileConfig = ConfigFileLoader.loadConfig(fileName);
    }

    public CFLintChainedConfig build() {
        // Exclude experimental by default
        if (defaultConfig.getIncludes().isEmpty() && defaultConfig.getExcludes().isEmpty()) {
            ruleGroups("!Experimental");
        }

        return new CFLintChainedConfig(defaultConfig).createNestedConfig(configFileConfig)
                .createNestedConfig(cmdLineConfig);
    }

    /**
     * Exclude these rule codes
     * 
     * @param codes
     */
    public void exclude(final List<String> codes) {
        if (cmdLineConfig == null) {
            cmdLineConfig = new CFLintConfig();
        }
        codes.forEach(code -> cmdLineConfig.addExclude(new PluginMessage(code)));
    }

    /**
     * Include these rule codes
     * 
     * @param codes
     */
    public void include(final List<String> codes) {
        if (cmdLineConfig == null) {
            cmdLineConfig = new CFLintConfig();
        }
        codes.forEach(code -> {
            cmdLineConfig.addInclude(new PluginMessage(code));
            cmdLineConfig.setInheritParent(false);
        });
    }
}