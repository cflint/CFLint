package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class TestJSONOutput {

	private JSONOutput outputer;
	private BugList bugList;
	private Writer writer;

	@Before
	public void setUp(){
		outputer = new JSONOutput();
		outputer.setPrettyPrint(false);
		bugList = new BugList(null);
		writer = new StringWriter();
	}

	@Test
	public void testOutput() throws IOException {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").setFilename("c:\\temp\\test.cfc").build();
		bugList.add(bugInfo);
		CFLintStats stats = new CFLintStats(123456L,1,new BigInteger("545454"));
		outputer.output(bugList, writer, false, stats);
		String expectedText = "[{\"severity\":\"\",\"id\":\"PARSE_ERROR\",\"message\":\"PARSE_ERROR\",\"category\":\"CFLINT\",\"abbrev\":\"PE\",\"locations\":[{\"file\":\"c:\\\\temp\\\\test.cfc\",\"fileName\":\"test.cfc\",\"function\":\"testf\",\"column\":\"1\",\"line\":\"1\",\"message\":\"\",\"variable\":\"\",\"expression\":\"\"}]}]";
//		assertEquals(JSONValue.parse(expectedText),JSONValue.parse(writer.toString()));
		assertEquals(expectedText,writer.toString());
	}

	@Test
	public void testStats() throws IOException {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").setFilename("c:\\temp\\test.cfc").build();
		bugList.add(bugInfo);
		CFLintStats stats = new CFLintStats(123456L,1,new BigInteger("545454"));
		outputer.output(bugList, writer, true, stats);
		String expectedText = "[{\"severity\":\"\",\"id\":\"PARSE_ERROR\",\"message\":\"PARSE_ERROR\",\"category\":\"CFLINT\",\"abbrev\":\"PE\",\"locations\":[{\"file\":\"c:\\\\temp\\\\test.cfc\",\"fileName\":\"test.cfc\",\"function\":\"testf\",\"column\":\"1\",\"line\":\"1\",\"message\":\"\",\"variable\":\"\",\"expression\":\"\"}]},{\"code\":\"PARSE_ERROR\",\"count\":\"1\"}]";
		assertEquals(expectedText,writer.toString());
	}
	
}
