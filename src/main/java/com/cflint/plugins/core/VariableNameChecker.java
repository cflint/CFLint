package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Attributes;
import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFVarDeclExpression;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import java.util.regex.Pattern;

@Extension
public class VariableNameChecker extends CFLintScannerAdapter {
	final String severity = "INFO";
		
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFVarDeclExpression) {
			final CFVarDeclExpression cfVarDeclExpression = (CFVarDeclExpression)expression;
			int lineNo = expression.getLine() + context.startLine() - 1;
			checkNameForBugs(cfVarDeclExpression.getName(), context.getFilename(), lineNo, bugs);
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
			
			checkNameForBugs(varName, context.getFilename(), lineNo, bugs);
		}
	}

	public void checkNameForBugs(String variable, String filename, int line, BugList bugs) {
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
				minVarLength = Integer.parseInt(getParameter("MaxLength"));
			} catch(Exception e) {}
		}

		if (getParameter("MaxWords") != null) {
			try {
				minVarLength = Integer.parseInt(getParameter("MaxWords"));
			} catch(Exception e) {}
		}

		ValidName name = new ValidName(minVarLength, maxVarLength, maxVarWords);

		if (name.isInvalid(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_INVALID_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " is not a valid name. Please use CamelCase or underscores.")
				.build());
		}
		if (name.isUpperCase(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_ALLCAPS_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should not be upper case.")
				.build());
		}
		if (name.tooShort(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_SHORT")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should be longer than " + minVarLength + " characters.")
				.build());
		}
		if (name.tooLong(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_LONG")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should be shorter than " + maxVarLength + " characters.")
				.build());
		}
		if (!name.isUpperCase(variable) && name.tooWordy(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_WORDY")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " is too wordy, can you think of a more concise name?")
				.build());
		}
		if (name.isTemporary(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_IS_TEMPORARY")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Temporary variable " + variable + " could be named better.")
				.build());
		}
		if (name.hasPrefixOrPostfix(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_HAS_PREFIX_OR_POSTFIX")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable has prefix or postfix " + variable + " and could be named better.")
				.build());
		}
	}
}