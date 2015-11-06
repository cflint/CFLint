package com.cflint.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoGroup;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/*
issues: {
	issue: {
		severity: "",
		id: "",
		message: "",
		category: "",
		abbrev: ""
	},
	location: {
		file: "",
		fileName: "",
		column: "",
		line: "",
		message: "",
		variable: ""
	},
	expression: ""
}
*/

public class TestCFLintConfig {

	final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
			"<CFLint-Plugin>\n" + 
			"    <group>\n" + 
			"        <ruleImpl name=\"OPM\">\n" + 
			"            <message code=\"code\">\n" + 
			"                <messageText>messageText</messageText>\n" + 
			"                <severity>WARNING</severity>\n" + 
			"            </message>\n" + 
			"        </ruleImpl>\n" + 
			"    </group>\n" + 
			"</CFLint-Plugin>";
	@Test
	public void test() throws Exception {
		CFLintPluginInfo config = new CFLintPluginInfo();
		config.setGroups(new ArrayList<CFLintPluginInfo.PluginInfoGroup>());
		PluginInfoGroup group = new PluginInfoGroup();
		config.getGroups().add(group);
		group.setRules(new ArrayList<CFLintPluginInfo.PluginInfoRule>());
		PluginInfoRule rule = new CFLintPluginInfo.PluginInfoRule();
		group.getRules().add(rule);
		rule.setName("OPM");
		PluginMessage message = new PluginMessage();
		rule.getMessages().add(message);
		message.setCode("code");
		message.setMessageText("messageText");
		message.setSeverity("WARNING");

		System.out.println(ConfigUtils.marshalJson(config));
		
		Marshaller jaxbMarshaller = ConfigUtils.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(config, sw);
		assertEquals(expected, sw.toString().trim());
		System.out.println(sw);
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
}
