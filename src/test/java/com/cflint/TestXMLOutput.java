package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class TestXMLOutput {

	private XMLOutput outputer;
	private BugList bugList;
	private Writer writer;

	@Before
	public void setUp(){
		outputer = new XMLOutput();
		bugList = new BugList(null);
		writer = new StringWriter();
	}

	@Test
	public void testOutput() throws IOException {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").setFilename("c:\\temp\\test.cfc").build();
		bugList.add(bugInfo);
		outputer.output(bugList, writer, false);
		String expectedText = "<issues version=\"" + Version.getVersion() + "\">\n" +
"<issue severity=\"\" id=\"PARSE_ERROR\" message=\"PARSE_ERROR\" category=\"CFLint\" abbrev=\"PE\"><location file=\"c:\\temp\\test.cfc\" fileName=\"test.cfc\" function=\"testf\" column=\"1\" line=\"1\" message=\"\" variable=\"\"><Expression><![CDATA[]]></Expression></location>\n" +
"</issue>\n" +
"</issues>";
		//remove the version 
		assertEquals(expectedText.replace("\n", "").replace("\r", ""),writer.toString().replace("\n", "").replace("\r", ""));
	}

	@Test
	public void testStats() throws IOException {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").setFilename("c:\\temp\\test.cfc").build();
		bugList.add(bugInfo);
		outputer.output(bugList, writer, true);
		String expectedText = "<issues version=\"" + Version.getVersion() + "\">\n" +
"<issue severity=\"\" id=\"PARSE_ERROR\" message=\"PARSE_ERROR\" category=\"CFLint\" abbrev=\"PE\"><location file=\"c:\\temp\\test.cfc\" fileName=\"test.cfc\" function=\"testf\" column=\"1\" line=\"1\" message=\"\" variable=\"\"><Expression><![CDATA[]]></Expression></location>\n" +
"</issue>\n" +
"<counts>\n" +
"<count code=\"PARSE_ERROR\" count=\"1\" />\n" +
"</counts>" +
"</issues>";
		assertEquals(expectedText.replace("\n", "").replace("\r", ""),writer.toString().replace("\n", "").replace("\r", ""));
	}
	
}
