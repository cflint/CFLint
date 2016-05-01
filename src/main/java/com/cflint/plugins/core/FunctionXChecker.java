package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class FunctionXChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	@Override
	public void expression(CFExpression expression, Context context,
			BugList bugs) {
		if (expression instanceof CFFunctionExpression) {
			final String cfmlFunctionCheck = getParameter("functionName");

			CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
			if (functionExpression.getName()
					.equalsIgnoreCase(cfmlFunctionCheck)) {
				int lineNo = expression.getLine() + context.startLine() - 1;
				// structNew(lineNo, context, bugs);
				context.addMessage(
						"AVOID_USING_" + cfmlFunctionCheck.toUpperCase(),
						cfmlFunctionCheck);

			}
		}
	}

}