package com.cflint.config;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;

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
		config.setRules(new ArrayList<CFLintPluginInfo.PluginInfoRule>());
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
		System.out.println(sw);
	}
}
