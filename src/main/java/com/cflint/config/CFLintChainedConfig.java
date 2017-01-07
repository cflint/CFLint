package com.cflint.config;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;

public class CFLintChainedConfig {

	final CFLintConfig config;
	final CFLintChainedConfig parent;

	public CFLintChainedConfig(CFLintConfig config) {
		super();
		this.config = config;
		parent = null;
	}
	public CFLintChainedConfig(CFLintConfig config,CFLintChainedConfig parent) {
		super();
		this.config = config;
		this.parent = parent;
	}
	
	public CFLintChainedConfig createNestedConfig(CFLintConfig config){
		return new CFLintChainedConfig(config,this);
	}
	
	public boolean includes(PluginMessage pluginMessage) {
		return config.includes(pluginMessage) || 
				(config.isInheritParent() && parent!=null && parent.includes(pluginMessage));
	}

	public Object excludes(PluginMessage pluginMessage) {
		return config.includes(pluginMessage) || 
				(config.isInheritParent() && parent!=null && parent.includes(pluginMessage));
	}
	
	public CFLintChainedConfig getParent() {
		return parent;
	}
	
}
