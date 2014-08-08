package com.parser.src.cfml.parsing;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.parser.main.cfscript.poundSignFilterStream;

public class TestPoundSignFilterStream {
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testPoundSignFilter() {
		java.util.ArrayList inputList = new java.util.ArrayList();
		
		inputList.add("writeoutput( \"##\" );");
		inputList.add("writeoutput( \"#a##b#\" );");
		inputList.add("writeoutput( \"#a#\" );");
		inputList.add("writeoutput( \"#a#more text\" );");
		inputList.add("writeoutput( \"more text#a#\" );");
		inputList.add("writeoutput( \"more text#a#more text\" );");
		inputList.add("writeoutput( \"more#a#text#a#more#a#text\" );");
		inputList.add("x = 1;\nwriteoutput( \"#x#\" );");
		inputList.add("/* this is a comment line */\na = 1;\nwriteoutput( a );");
		inputList.add("/* this is a comment line */\na = 1;\nwriteoutput( a );");
		inputList.add("arrayAppend( variables.framework.routes, { '#method##route#' : target } );");
		StringBuffer lastTest = new StringBuffer();
		for (int i = 0; i < inputList.size(); i++) {
			lastTest.append((String) inputList.get(i));
			lastTest.append("\r\n");
		}
		inputList.add(lastTest.toString());
		inputList.add("/*aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				+ "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				+ "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				+ "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				+ "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa*/");
		
		for (int i = 0; i < inputList.size(); i++) {
			String input = (String) inputList.get(i);
			try {
				poundSignFilterStream psf = new poundSignFilterStream(new java.io.StringReader(input));
				char[] buffer = new char[1024];
				int read = psf.read(buffer);
				System.out.println("BEFORE: " + input.length() + "; AFTER: " + read + "; Added: " + psf.getAdded()
						+ "; Total = " + (read - psf.getAdded()));
				System.out.println(input + " => " + new String(buffer, 0, read));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
