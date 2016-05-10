package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFVarDeclExpression;
import ro.fortsoft.pf4j.Extension;

@Extension
public class VariableNameChecker extends CFLintScannerAdapter {
	public static final String VARIABLE = "Variable ";
	final String severity = "INFO";
		
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFVarDeclExpression) {
			final CFVarDeclExpression cfVarDeclExpression = (CFVarDeclExpression)expression;
			int lineNo = expression.getLine() + context.startLine() - 1;
			checkNameForBugs(context,cfVarDeclExpression.getName(), context.getFilename(), context.getFunctionName(), lineNo, bugs);
		}
		else if (expression instanceof CFFullVarExpression) {
			final CFFullVarExpression cfFullVarExpression = (CFFullVarExpression)expression;
			for(final CFExpression subexpression : cfFullVarExpression.getExpressions()){
				expression(subexpression,context,bugs);
			}
		}
		else if (expression instanceof CFIdentifier) {
			String varName = ((CFIdentifier) expression).getName();
			int lineNo = ((CFIdentifier) expression).getLine() + context.startLine() - 1;
			
			checkNameForBugs(context, varName, context.getFilename(), context.getFunctionName(), lineNo, bugs);
		}
	}

	public void checkNameForBugs(final Context context, String variable, String filename, String functionName, int line, BugList bugs) {
		int minVarLength = ValidName.MIN_VAR_LENGTH;
		int maxVarLength = ValidName.MAX_VAR_LENGTH;
		int maxVarWords = ValidName.MAX_VAR_WORDS;

		if (getParameter("MinLength") != null) {
			try {
				minVarLength = Integer.parseInt(getParameter("MinLength"));
			} catch(Exception e) {}
		}

		if (getParameter("MaxLength") != null) {
			try {
				maxVarLength = Integer.parseInt(getParameter("MaxLength"));
			} catch(Exception e) {}
		}

		if (getParameter("MaxWords") != null) {
			try {
				maxVarWords = Integer.parseInt(getParameter("MaxWords"));
			} catch(Exception e) {}
		}

		CFScopes scope = new CFScopes();
		ValidName name = new ValidName(minVarLength, maxVarLength, maxVarWords);

		if (name.isInvalid(variable)) {
			context.addMessage("VAR_INVALID_NAME", variable);
		}
		if (!scope.isCFScoped(variable) && name.isUpperCase(variable)) {
			context.addMessage("VAR_ALLCAPS_NAME", variable);
		}
		if (scope.isCFScoped(variable)
				&& name.isUpperCase(variable)
				&& (getParameter("IgnoreUpperCaseScopes") == null || !getParameter(
						"IgnoreUpperCaseScopes").contains(variable))) {
			context.addMessage("SCOPE_ALLCAPS_NAME", variable);
		}
		if (name.tooShort(variable)) {
			context.addMessage("VAR_TOO_SHORT", variable);
		}
		if (name.tooLong(variable)) {
			context.addMessage("VAR_TOO_LONG", variable);
		}
		if (!name.isUpperCase(variable) && name.tooWordy(variable)) {
			context.addMessage("VAR_TOO_WORDY", variable);
		}
		if (name.isTemporary(variable)) {
			context.addMessage("VAR_IS_TEMPORARY", variable);
		}
		if (name.hasPrefixOrPostfix(variable)) {
			context.addMessage("VAR_HAS_PREFIX_OR_POSTFIX", variable);
		}
	}
}