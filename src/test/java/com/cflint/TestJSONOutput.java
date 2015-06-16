package com.cflint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

import com.cflint.tools.CFLintFilter;

public class TestJSONOutput {

	private JSONOutput outputer;
	private BugList bugList;
	private Writer writer;
	@Before
	public void setUp(){
		outputer = new JSONOutput();
		bugList = new BugList(null);
		writer = new StringWriter();
	}
	@Test
	public void testOutput1() throws IOException {
		BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR").setFilename("c:\\temp\\test.cfc").build();
		bugList.add(bugInfo);
		outputer.output(bugList, writer);
		assertEquals("[{\"severity\":\"\",\"locations\":[{\"fileName\":\"test.cfc\",\"file\":\"c:\\\\temp\\\\test.cfc\",\"expression\":\"\",\"line\":\"0\",\"column\":\"0\",\"variable\":\"\",\"message\":\"\"}],\"id\":\"PARSE_ERROR\",\"abbrev\":\"PE\",\"message\":\"PARSE_ERROR\",\"category\":\"CFLINT\"}]",
				writer.toString());
	}
}
