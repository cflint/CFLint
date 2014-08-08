package com.parser.main.cfscript;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.RecognitionException;

public interface IErrorReporter {
	void reportError(String error);
	
	void reportError(RecognitionException re);
	
	void reportError(String[] tokenNames, RecognitionException e);
	
	void reportError(IntStream input, RecognitionException re, BitSet follow);
}
