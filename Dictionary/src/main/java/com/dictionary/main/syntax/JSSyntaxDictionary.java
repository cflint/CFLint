/*
 * Created on Jan 31, 2004
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
 *         This is the javascript specific syntax dictionary dictionary
 */
public class JSSyntaxDictionary extends SyntaxDictionary implements ISyntaxDictionary {
	// protected static Map functions;
	protected static Set operators;
	protected static Set scriptkeywords;
	
	public JSSyntaxDictionary() {
		super();
		buildFunctionSyntax();
		
		operators = new HashSet();
		buildOperatorSyntax();
		
		scriptkeywords = new HashSet();
		buildScriptKeywordSyntax();
	}
	
	/**
	 * gets any operators (lowercase only)
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
	 * gets all the functions (lowercase only)
	 * 
	 * @param elementname
	 * @return
	 */
	public Set getFunctions() {
		return functions.keySet();
	}
	
	/**
	 * retuns a functions usage
	 * 
	 * @param functionname
	 * @return
	 */
	public String getFunctionUsage(String functionname) {
		return (String) functions.get(functionname.toLowerCase());
	}
	
	public Set getAllElements() {
		return null;
	}
	
	public Set getElementAttributes(String ele) {
		return null;
	}
	
	public Set getFilteredAttributes(String x, String y) {
		return null;
	}
	
	public Set getFilteredElements(String ele) {
		return null;
	}
	
	// /////////////////////////////////////////////////////////////////////////
	/** build all the cfscript keywords */
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
		scriptkeywords.add("abstract");
		scriptkeywords.add("extends");
		scriptkeywords.add("int");
		scriptkeywords.add("super");
		scriptkeywords.add("boolean");
		scriptkeywords.add("false");
		scriptkeywords.add("interface");
		scriptkeywords.add("break");
		scriptkeywords.add("final");
		scriptkeywords.add("long");
		scriptkeywords.add("synchronized");
		scriptkeywords.add("byte");
		scriptkeywords.add("finally");
		scriptkeywords.add("native");
		scriptkeywords.add("this");
		scriptkeywords.add("float");
		scriptkeywords.add("new");
		scriptkeywords.add("throw");
		scriptkeywords.add("null");
		scriptkeywords.add("throws");
		scriptkeywords.add("char");
		scriptkeywords.add("package");
		scriptkeywords.add("transient");
		scriptkeywords.add("class");
		scriptkeywords.add("goto");
		scriptkeywords.add("true");
		scriptkeywords.add("const");
		scriptkeywords.add("protected");
		scriptkeywords.add("implements");
		scriptkeywords.add("public");
		scriptkeywords.add("import");
		scriptkeywords.add("val");
		// scriptkeywords.add("in");
		scriptkeywords.add("short");
		scriptkeywords.add("double");
		scriptkeywords.add("instanceof");
		scriptkeywords.add("static");
		scriptkeywords.add("with");
	}
	
	/** build all the operators in the language */
	protected static void buildOperatorSyntax() {
		operators.add("+");
		operators.add("-");
		operators.add("*");
		operators.add("/");
		operators.add("=");
		operators.add("!");
		operators.add(">");
		operators.add(">=");
		operators.add("<");
		operators.add("<=");
		operators.add("&");
		operators.add("&&");
		operators.add("|");
		operators.add("||");
		operators.add(".");
		operators.add(":");
		operators.add("?");
		operators.add("++");
		operators.add("--");
		operators.add("==");
		operators.add("!=");
		operators.add("+=");
		operators.add("-=");
		operators.add("*=");
		operators.add("/=");
		operators.add("|=");
		operators.add("<<");
		operators.add(">>");
	}
	
	/** build all the functions in the language */
	protected void buildFunctionSyntax() {
		// these are only top level functions -- could do more I guess
		// but that could get hairy
		/*
		 * functions.put("escape","String Escape(String)"); functions.put("eval","Object Eval(codeString)");
		 * functions.put("isFinite","boolean isFinite(testnumber)"); functions.put("isNaN","boolean isNaN(testvalue)");
		 * functions.put("number","number Number(Object)"); functions.put("parseFloat","float parseFloat(String)");
		 * functions.put( "parseInt","int parseInt(String, radix) || int parseInt(String)");
		 * functions.put("string","boolean String(Object)"); functions.put("unescape","String Unescape(encodedString)");
		 * 
		 * functions.put("alert","void alert(String)"); functions.put("confirm","boolean confirm(String)");
		 */
	}
}
