package com.cflint.plugins.core;

import java.util.HashSet;
import java.util.Set;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFVarDeclExpression;
import cfml.parsing.cfscript.cfFullVarExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;

public class ArgVarChecker implements CFLintScanner {

	/**
	 * Report each occurrence once per file/function
	 */
	Set<String> alreadyReported1 = new HashSet<String>();
	Set<String> alreadyReported2 = new HashSet<String>();

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
				bugs.add(new BugInfo.BugInfoBuilder().setMessageCode("ARG_VAR_CONFLICT")
						.setMessage("Variable " + name + " should not be declared in both var and argument scopes.")
						.setVariable(name).setFunction(context.getFunctionName()).setSeverity("ERROR")
						.setFilename(context.getFilename()).setExpression(expression.Decompile(0))
						.build(expression, context.getElement()));
			}
		} else if (expression instanceof cfFullVarExpression) {
			final cfFullVarExpression fullVarExpr = (cfFullVarExpression) expression;
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
						bugs.add(new BugInfo.BugInfoBuilder()
								.setMessageCode("ARG_VAR_MIXED")
								.setMessage(
										"Variable " + name + " should not be referenced in local and argument scope.")
								.setVariable(name).setFunction(context.getFunctionName()).setSeverity("INFO")
								.setFilename(context.getFilename()).setExpression(expression.Decompile(0))
								.build(expression, context.getElement()));

					}
				}
			}
			expression(fullVarExpr.getExpressions().get(0), context, bugs);
		}
	}

	public void element(final Element element, final Context context, final BugList bugs) {

	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}

}
