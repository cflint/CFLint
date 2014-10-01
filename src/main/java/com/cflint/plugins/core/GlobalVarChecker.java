package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Collection;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.cfFullVarExpression;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class GlobalVarChecker extends CFLintScannerAdapter {

	final static Collection<String> variables = Arrays.asList("APPLICATION", "CGI", "COOKIE", "FORM", "REQUEST",
			"SERVER", "SESSION", "URL");

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if ((context.isInComponent() || context.isInFunction()) && expression instanceof cfFullVarExpression) {
			final CFExpression firstExpression = ((cfFullVarExpression) expression).getExpressions().get(0);
			if (firstExpression instanceof CFIdentifier) {
				doIdentifier((CFIdentifier) firstExpression, context, bugs);
			}
		}
	}

	protected void doIdentifier(final CFIdentifier expression, final Context context, final BugList bugs) {
		final String name = expression.getName();
		if (variables.contains(name.toUpperCase()) && !context.getCallStack().isVariable(name)
				&& !context.getCallStack().isArgument(name)
				&& context.getCallStack().getPluginVar(GlobalVarChecker.class, name) == null) {
			context.getCallStack().setPluginVar(GlobalVarChecker.class, name, true);
			context.addMessage("GLOBAL_VAR", name);
		}

	}

}
