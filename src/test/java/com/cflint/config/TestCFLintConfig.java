package com.cflint.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintPluginInfo.RuleGroup;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;


public class TestCFLintConfig {

	final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
			"<CFLint-Plugin>\n" + 
			"    <ruleImpl name=\"OPM\">\n" + 
			"        <message code=\"code\">\n" + 
			"            <messageText>messageText</messageText>\n" + 
			"            <severity>WARNING</severity>\n" + 
			"        </message>\n" + 
			"    </ruleImpl>\n" + 
			"</CFLint-Plugin>";
	@Test
	public void test() throws Exception {
		CFLintPluginInfo config = new CFLintPluginInfo();
		PluginInfoRule rule = new CFLintPluginInfo.PluginInfoRule();
		config.getRules().add(rule);
		rule.setName("OPM");
		PluginMessage message = new PluginMessage();
		rule.getMessages().add(message);
		message.setCode("code");
		message.setMessageText("messageText");
		message.setSeverity("WARNING");

		Marshaller jaxbMarshaller = ConfigUtils.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(config, sw);
		assertEquals(expected, sw.toString().trim());
	}
	
	@Test
	public void test2() throws IOException{
    		StringWriter writer = new StringWriter();
    	JsonFactory jsonF = new JsonFactory();
    	JsonGenerator jg = jsonF.createGenerator(writer);
    	jg.writeStartArray();
    
    	jg.writeEndArray();
    	jg.close();
    	writer.close();
    	System.out.println(writer);
    }
	
	@Test
	/**
	 * Test the round trip of the config json file including rule groups.
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void testRuleGroups() throws JsonGenerationException, JsonMappingException, IOException{
	       CFLintPluginInfo config = new CFLintPluginInfo();
	        PluginInfoRule rule = new CFLintPluginInfo.PluginInfoRule();
	        config.getRules().add(rule);
	        rule.setName("OPM");
	        PluginMessage message = new PluginMessage();
	        rule.getMessages().add(message);
	        message.setCode("MyCode");
	        message.setMessageText("messageText");
	        message.setSeverity("WARNING");
	        RuleGroup ruleGroup = new RuleGroup("r1");
	        ruleGroup.setDefaultSeverity("INFO");
	        ruleGroup.getMessages().add(message);
	        config.getRuleGroups().add(ruleGroup);
            RuleGroup ruleGroup2 = new RuleGroup("r2");
            config.getRuleGroups().add(ruleGroup2);
	        String jsonText = ConfigUtils.marshalJson(config);
	        System.out.println(jsonText);
	        CFLintPluginInfo backConfig = ConfigUtils.unmarshalJson(jsonText,CFLintPluginInfo.class);
            assertEquals("MyCode",backConfig.getRules().get(0).getMessages().get(0).getCode());
            assertEquals("messageText",backConfig.getRules().get(0).getMessages().get(0).getMessageText());
	        assertEquals("MyCode",backConfig.getRuleGroups().get(0).getMessages().get(0).getCode());
            assertEquals("messageText",backConfig.getRuleGroups().get(0).getMessages().get(0).getMessageText());
            assertEquals("INFO",backConfig.getRuleGroups().get(0).getDefaultSeverity());
	}
}
