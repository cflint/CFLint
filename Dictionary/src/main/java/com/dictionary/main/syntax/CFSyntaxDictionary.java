/*
 * Created on Jan 30, 2004
 *
 * The MIT License
 * Copyright (c) 2004 Rob Rohan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software 
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package com.dictionary.main.syntax;

import java.util.HashSet;
import java.util.Set;

import com.dictionary.main.ISyntaxDictionary;
import com.dictionary.main.SyntaxDictionary;

/**
 * @author Rob
 * 
 *         Extension of the SyntaxDictionary. Has cf specific items. This class is not used directly in cfe. There is
 *         one more level in the SQLSyntaxDictionary
 * 
 * @see SQLSyntaxDictionary
 */
public class CFSyntaxDictionary extends SyntaxDictionary implements ISyntaxDictionary {
	protected static Set operators;
	protected static Set scriptkeywords;
	
	public CFSyntaxDictionary() {
		super();
		operators = new HashSet();
		buildOperatorSyntax();
		
		scriptkeywords = new HashSet();
		buildScriptKeywordSyntax();
	}
	
	/**
	 * gets any operators (eq, or, and) (lowercase only)
	 * 
	 * @param elementname
	 * @return
	 */
	public Set getOperators() {
		return operators;
	}
	
	/**
	 * gets cfscript specific keywords (if, while, etc);
	 * 
	 * @return
	 */
	public Set getScriptKeywords() {
		return scriptkeywords;
	}
	
	/**
	 * build all the cfscript keywords
	 */
	protected static void buildScriptKeywordSyntax() {
		scriptkeywords.add("for");
		scriptkeywords.add("if");
		scriptkeywords.add("else");
		scriptkeywords.add("while");
		scriptkeywords.add("return");
		scriptkeywords.add("function");
		scriptkeywords.add("var");
		scriptkeywords.add("case");
		scriptkeywords.add("do");
		scriptkeywords.add("try");
		scriptkeywords.add("catch");
		scriptkeywords.add("continue");
		scriptkeywords.add("switch");
		scriptkeywords.add("default");
		scriptkeywords.add("break");
		scriptkeywords.add("true");
		scriptkeywords.add("false");
		scriptkeywords.add("to");
		scriptkeywords.addAll(operators);
	}
	
	/**
	 * build all the operators in the language
	 */
	protected static void buildOperatorSyntax() {
		operators.add("gt");
		operators.add("lt");
		operators.add("gte");
		operators.add("lte");
		operators.add("eq");
		operators.add("neq");
		operators.add("not");
		operators.add("and");
		operators.add("or");
		operators.add("mod");
		operators.add("is");
		operators.add("does");
		operators.add("contains");
		operators.add("greater");
		operators.add("than");
		operators.add("less");
		operators.add("equal");
		// operators.add("to");
		operators.add("xor");
		operators.add("eqv");
		operators.add("imp");
	}
}
