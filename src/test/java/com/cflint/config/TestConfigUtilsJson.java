package com.cflint.config;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.Marshaller;

import org.junit.Test;

public class TestConfigUtilsJson {

	@Test
	public void test() throws Exception {
		CFLintPluginInfo config = ConfigUtils.unmarshal(this.getClass().getResourceAsStream("/cflint.definition.xml"), CFLintPluginInfo.class);
		
		String jsondata = ConfigUtils.marshalJson(config);
		System.out.println(jsondata);
		
		config = ConfigUtils.unmarshalJson(new StringReader(jsondata),CFLintPluginInfo.class);
		
		Marshaller jaxbMarshaller = ConfigUtils.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(config, sw);
		System.out.println(sw);
	}
}
