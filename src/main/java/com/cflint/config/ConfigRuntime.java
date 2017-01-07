package com.cflint.config;

import java.util.ArrayList;
import java.util.List;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.CFLintScanner;
import com.cflint.tools.CFTool;

/**
 * Combines information from the config.xml and the core CFLint information and
 * provides utility functions
 *
 * It only includes the Rules that are matched by the include/exclude section if
 * one of them is present.
 */
public class ConfigRuntime extends CFLintConfig {

	public ConfigRuntime() {

	}

	public ConfigRuntime(final CFLintConfig config, final CFLintPluginInfo pluginInfo) {
		final List<PluginInfoRule> rules = new ArrayList<PluginInfoRule>();

		if (config != null) {
			includes.addAll(config.getIncludes());
			excludes.addAll(config.getExcludes());
			rules.addAll(config.getRules());
		}
		for (final PluginInfoRule rule : pluginInfo.getRules()) {
			if (!rules.contains(rule)) {
				rules.add(rule);
			}
		}

		if (config != null) {
			// If includes is specified, load *only* those messages
			if (!config.getIncludes().isEmpty()) {
				for (final PluginInfoRule rule : rules) {
					// Include the rule if at least one of the messages is
					// included.
					for (final PluginMessage msg : rule.getMessages()) {
						if (config.getIncludes().contains(msg)) {
							for (final PluginMessage cfgMsg : config.getIncludes()) {
								if (cfgMsg.equals(msg)) {
									CFTool.merge(cfgMsg, msg);
								}
							}

							getRules().add(rule);
							break;
						}
					}
				}
			} else {// Otherwise load all considering the excludes.
				for (final PluginInfoRule rule : rules) {
					// Exclude the rule if ALL of the messages are excluded.
					boolean excluded = true;
					for (final PluginMessage msg : rule.getMessages()) {
						excluded = excluded && config.getExcludes().contains(msg);
					}
					if (!excluded) {
						getRules().add(rule);
					}
				}
			}
		} else {
			getRules().addAll(rules);
		}
	}




}
