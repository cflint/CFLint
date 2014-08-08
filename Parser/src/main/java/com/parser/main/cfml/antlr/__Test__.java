package com.parser.main.cfml.antlr;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class __Test__ {
	
	public static void main(String args[]) throws Exception {
		CFLexer lex = new CFLexer(new ANTLRFileStream(
				"/Users/valliant/Projects/java/CFML/com.parser.main/src/cfml/parsing/cfml/antlr/./__Test___input.txt",
				"UTF8"));
		CommonTokenStream tokens = new CommonTokenStream(lex);
		
		CFParser g = new CFParser(tokens, null);
		try {
			g.compilationUnit();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
	}
}