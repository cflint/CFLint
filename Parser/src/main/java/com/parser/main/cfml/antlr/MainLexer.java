package com.parser.main.cfml.antlr;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.Token;

public class MainLexer {
	public static void main(String[] args) {
		try {
			CharStream input = new ANTLRFileStream("./src/cfml/parsing/cfml/antlr/input3");
			XMLLexer lexer = new XMLLexer(input);
			Token token = lexer.nextToken();
			while (token.getType() != Token.EOF) {
				System.out.println("Token: " + token.getText());
				token = lexer.nextToken();
			}
			
		} catch (Throwable t) {
			System.out.println("Exception: " + t);
			t.printStackTrace();
		}
	}
}