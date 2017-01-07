package com.cflint.config;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;


public class TestCFLintChainedConfig {

	final String sconfig0 = "<config><includes code=\"ARG_VAR_CONFLICT\"/></config>";
	final String sconfig1 = "<config><includes code=\"COMPONENT_HINT_MISSING\"/></config>";
	final String sconfig2 = "<config inheritParent=\"false\"><includes code=\"FUNCTION_HINT_MISSING\"/></config>";
	final String sconfig3 = "<config><includes code=\"ARG_HINT_MISSING\"/></config>";
	private CFLintConfig config;
	private CFLintConfig config1;
	private CFLintConfig config2;
	private CFLintChainedConfig nestConfig1;
	private CFLintChainedConfig nestConfig2;
	private CFLintChainedConfig nestConfig2b;
	@Before
	public void setup() throws Exception{
		config = ConfigUtils.unmarshal(sconfig0, CFLintConfig.class);
		config1 = ConfigUtils.unmarshal(sconfig1, CFLintConfig.class);
		config2 = ConfigUtils.unmarshal(sconfig2, CFLintConfig.class);
		nestConfig1 = new CFLintChainedConfig(config);
		nestConfig2 = nestConfig1.createNestedConfig(config1);
		nestConfig2b = nestConfig1.createNestedConfig(config2);
	}
	
	@Test
	public void test() throws Exception {
		
		assertTrue(nestConfig1.includes(new PluginMessage("ARG_VAR_CONFLICT")));
		assertTrue(!nestConfig2b.includes(new PluginMessage("ARG_VAR_CONFLICT")));
		assertTrue(!nestConfig1.includes(new PluginMessage("FUNCTION_HINT_MISSING")));
		assertTrue(nestConfig2b.includes(new PluginMessage("FUNCTION_HINT_MISSING")));
	}
	
	@Test
	public void test2() throws Exception {
		
		assertTrue(nestConfig1.includes(new PluginMessage("ARG_VAR_CONFLICT")));
		assertTrue(nestConfig2.includes(new PluginMessage("ARG_VAR_CONFLICT")));
		assertTrue(!nestConfig1.includes(new PluginMessage("COMPONENT_HINT_MISSING")));
		assertTrue(nestConfig2.includes(new PluginMessage("COMPONENT_HINT_MISSING")));
	}
	
}
