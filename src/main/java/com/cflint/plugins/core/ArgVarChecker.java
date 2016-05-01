package com.cflint.plugins.core;

import java.util.HashSet;
import java.util.Set;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFVarDeclExpression;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class ArgVarChecker extends CFLintScannerAdapter {

	/**
	 * Report each occurrence once per file/function
	 */
	Set<String> alreadyReported1 = new HashSet<String>();
	Set<String> alreadyReported2 = new HashSet<String>();

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFVarDeclExpression) {
			final String name = ((CFVarDeclExpression) expression).getName();
			if (context.isInFunction() && context.getCallStack().hasArgument(name)) {
				final String fileKey = context.fileFunctionString();
				if (alreadyReported1.contains(fileKey)) {
					return;
				} else {
					alreadyReported1.add(fileKey);
				}
				context.addMessage("ARG_VAR_CONFLICT", name);
			}
		} else if (expression instanceof CFFullVarExpression) {
			final CFFullVarExpression fullVarExpr = (CFFullVarExpression) expression;
			if (fullVarExpr.getExpressions().size() > 1 && fullVarExpr.getExpressions().get(0) instanceof CFIdentifier) {
				final CFIdentifier cfIdentifier1 = (CFIdentifier) fullVarExpr.getExpressions().get(0);
				if (cfIdentifier1.getName().equalsIgnoreCase("arguments")
						&& fullVarExpr.getExpressions().get(1) instanceof CFIdentifier) {
					final CFIdentifier cfIdentifier2 = (CFIdentifier) fullVarExpr.getExpressions().get(1);
					final String name = cfIdentifier2.getName();
					if (context.getCallStack().isVariable(name)) {
						final String fileKey = context.fileFunctionString();
						if (alreadyReported2.contains(fileKey)) {
							return;
						} else {
							alreadyReported2.add(fileKey);
						}
						context.addMessage("ARG_VAR_MIXED", name);
					}
				}
			}
			expression(fullVarExpr.getExpressions().get(0), context, bugs);
		}
	}

}
