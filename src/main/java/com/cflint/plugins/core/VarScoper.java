package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.cfFullVarExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;

public class VarScoper implements CFLintScanner {

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFIdentifier) {
			final String name = ((CFIdentifier) expression).getName();
			if (context.isInFunction() && context.isInAssignmentExpression()
					&& !context.getCallStack().checkVariable(name)) {
				bugs.add(new BugInfo.BugInfoBuilder().setMessageCode("MISSING_VAR")
						.setMessage("Variable " + name + " is not declared with a var statement.").setVariable(name)
						.setFunction(context.getFunctionName()).setSeverity("ERROR").setFilename(context.getFilename())
						.setExpression(expression.Decompile(0)).build(expression, context.getElement()));
			} else if (expression instanceof cfFullVarExpression) {
				final cfFullVarExpression fullVarExpr = (cfFullVarExpression) expression;
				expression(fullVarExpr.getExpressions().get(0), context, bugs);
			}
		}
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		// TODO Auto-generated method stub

	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}

}
