package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.CFIfStatement;
import cfml.parsing.cfscript.script.CFExpressionStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class ComplexBooleanExpressionChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";
	
	protected int complexThreshold = 10;

	@Override	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFBinaryExpression) {
			String code = expression.Decompile(0).toLowerCase();

			if (isComplex(code, complexThreshold)) {
				int lineNo = ((CFBinaryExpression) expression).getLine() + context.startLine() - 1;

				complexBooleanExpression(lineNo, context, bugs);
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		String tag = element.getName();

		if (tag.equals("cfif")) { 
			String content = element.getStartTag().getTagContent().toString();

			if (isComplex(content, complexThreshold)) {
				int lineNo = element.getSource().getRow(element.getBegin());

				complexBooleanExpression(lineNo, context, bugs);
			}
		}
	}

	protected boolean isComplex(final String code, final int complexThreshold) {
		int noAnds = noSubstrings(code, " && ") + noSubstrings(code, " and ");
		int noOrs = noSubstrings(code, " || ") + noSubstrings(code, " or ");
		int noNots = noSubstrings(code, " ! ") + noSubstrings(code, " not ");

		// This is just a rough heuristic
		// lots of and's or or's on their own is usually easy to understand
		// but combine them together and it gets hard to understand very quickly 
		return (noAnds * noOrs + noNots + noAnds + noOrs) > complexThreshold;
	}

	protected int noSubstrings(String string, String substring) {
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {
		    lastIndex = string.indexOf(substring, lastIndex);

		    if (lastIndex != -1) { 
		        count ++;
		        lastIndex += substring.length();
		    }
		}

		return count;
	}

	public void complexBooleanExpression(final int lineNo, final Context context, final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("COMPLEX_BOOLEAN_CHECK")
			.setSeverity(severity).setFilename(context.getFilename())
			.setMessage("Boolean expession at " + lineNo + " is too complex. Consider simplifying or moving to a named method.")
			.build());
	}

}
