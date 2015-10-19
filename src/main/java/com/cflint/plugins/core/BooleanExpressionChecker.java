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

public class BooleanExpressionChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	protected int lastLineNo = -1;

	@Override	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFBinaryExpression) {
			String code = expression.Decompile(0).toLowerCase();

			if (hasExplicitBooleanCheck(code)) {
				int lineNo = currentLine(expression, context);

				// Only report issue once per line
				if (lastLineNo != lineNo) {
					booleanExpression(lineNo, context, bugs);
					lastLineNo = lineNo;
				}
			}
		}
		
	}

	protected boolean hasExplicitBooleanCheck(final String code) {
		return code.contains("== true") || code.contains("eq true") || code.contains("is true") || code.contains("!= true")
			|| code.contains("== false") || code.contains("eq false") || code.contains("is false") || code.contains("!= false");
	}

	public void booleanExpression(final int lineNo, final Context context, final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("EXPLICIT_BOOLEAN_CHECK")
			.setSeverity(severity).setFilename(context.getFilename())
			.setMessage("Explicit check of boolean expession at " + lineNo + " is not needed.")
			.build());
	}

}
