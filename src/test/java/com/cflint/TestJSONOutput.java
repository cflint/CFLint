package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

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
		String expectedText = "[{\"severity\":\"\",\"id\":\"PARSE_ERROR\",\"message\":\"PARSE_ERROR\",\"category\":\"CFLINT\",\"abbrev\":\"PE\",\"location\":{\"file\":\"c:\\\\temp\\\\test.cfc\",\"fileName\":\"test.cfc\",\"column\":\"0\",\"line\":\"0\",\"message\":\"\",\"variable\":\"\",\"expression\":\"\"}}]";
//		assertEquals(JSONValue.parse(expectedText),JSONValue.parse(writer.toString()));
		assertEquals(expectedText,writer.toString());
	}
	
	@Test
	public void testRead() throws JsonParseException, JsonMappingException, IOException{
		InputStream is = new FileInputStream("src\\main\\config\\cflintexclude.json");
		System.out.println(is);
		//HashMap<String, Object> props = (HashMap<String,Object>) new ObjectMapper().readValue(this.getClass().getResourceAsStream("/config/cflintexcludes.json"), HashMap.class);
		
		ObjectMapper mapper = new ObjectMapper();

		ArrayList list = mapper.readValue(is, ArrayList.class);
		System.out.println(list);
		System.out.println(((Map)list.get(0)).get("function"));
	}
}
