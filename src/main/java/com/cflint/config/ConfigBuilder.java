package com.cflint.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
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
     * @param rulegroups        rule groups
     * @return                  this ConfigBuilder
     */
    public ConfigBuilder ruleGroups(final String rulegroups) {
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
        return this;
    }

    /**
     * Add a custom configuration on top of the baseline. Hint: most
     * configurations do not need this.
     * 
     * @param fileName          the custom config file
     * @return                  this ConfigBuilder
     * @throws  Exception       exception when configuration fails
     */
    public ConfigBuilder addCustomConfig(final String fileName) throws Exception {
        File file = new File(fileName);
        configFileConfig = file.getName().toLowerCase().endsWith(".xml")
                ? ConfigUtils.unmarshal(file, CFLintConfig.class)
                : ConfigUtils.unmarshalJson(new FileInputStream(file), CFLintConfig.class);

        return this;
    }

    /**
     *  Builds the configuration object.
     * @return                  this ConfigBuilder
     */
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
     * @param codes             list of rule codes to exclude
     * @return                  this ConfigBuilder
     */
    public ConfigBuilder exclude(final List<String> codes) {
        if (cmdLineConfig == null) {
            cmdLineConfig = new CFLintConfig();
        }
        codes.forEach(code -> cmdLineConfig.addExclude(new PluginMessage(code)));
        return this;
    }

    /**
     * Include these rule codes
     * 
     * @param codes             list of rule codes to include
     * @return                  this ConfigBuilder
     */
    public ConfigBuilder include(final List<String> codes) {
        if (cmdLineConfig == null) {
            cmdLineConfig = new CFLintConfig();
        }
        codes.forEach(code -> {
            cmdLineConfig.addInclude(new PluginMessage(code));
            cmdLineConfig.setInheritParent(false);
        });
        return this;
    }
    
    /**
     * Include these rule codes
     * 
     * @param codes             list of rule codes to include
     * @return                  this ConfigBuilder
     */
    public ConfigBuilder include(final String ... codes){
        return include(Arrays.asList(codes));
    }
    
    /**
     * Exclude these rule codes
     * 
     * @param codes             list of rule codes to exclude
     * @return                  this ConfigBuilder
     */
    public ConfigBuilder exclude(final String ... codes){
        return exclude(Arrays.asList(codes));
    }
}