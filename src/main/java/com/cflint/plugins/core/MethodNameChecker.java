package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Attributes;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;


import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import java.util.regex.Pattern;

@Extension
public class MethodNameChecker extends CFLintScannerAdapter {
	final String severity = "INFO";
	
	@Override	
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement method = (CFFuncDeclStatement) expression;
			int lineNo = method.getLine() + context.startLine() - 1;
			checkNameForBugs(context.getFunctionName(), context.getFilename(), lineNo, bugs);
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cffunction")) {
			int lineNo = element.getSource().getRow(element.getBegin());
			checkNameForBugs(context.getFunctionName(), context.getFilename(), lineNo, bugs);
		}
	}

	public void checkNameForBugs(String method, String filename, int line, BugList bugs) {
		int minMethodLength = ValidName.MIN_METHOD_LENGTH;
		int maxMethodLength = ValidName.MAX_METHOD_LENGTH;
		int maxMethodWords = ValidName.MAX_METHOD_WORDS;

		if (getParameter("MinLength") != null) {
			try {
				minMethodLength = Integer.parseInt(getParameter("MinLength"));
			} catch(Exception e) {}
		}

		if (getParameter("MaxLength") != null) {
			try {
				maxMethodLength = Integer.parseInt(getParameter("MaxLength"));
			} catch(Exception e) {}
		}

		if (getParameter("MaxWords") != null) {
			try {
				maxMethodWords = Integer.parseInt(getParameter("MaxWords"));
			} catch(Exception e) {}
		}

		ValidName name = new ValidName(minMethodLength, maxMethodLength, maxMethodWords);

		if (name.isInvalid(method)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("METHOD_INVALID_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Method name " + method + " is not a valid name. Please use CamelCase or underscores.")
				.build());
		}
		if (name.isUpperCase(method)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("METHOD_ALLCAPS_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Method name " + method + " should not be upper case.")
				.build());
		}
		if (name.tooShort(method)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("METHOD_TOO_SHORT")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Method name " + method + " should be longer than " + minMethodLength + " characters.")
				.build());
		}
		if (name.tooLong(method)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("METHOD_TOO_LONG")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Method name " + method + " should be shorter than " + maxMethodLength + " characters.")
				.build());
		}
		if (!name.isUpperCase(method) && name.tooWordy(method)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("METHOD_TOO_WORDY")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Method name " + method + " is too wordy, can you think of a more concise name?")
				.build());
		}
		if (name.isTemporary(method)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("METHOD_IS_TEMPORARY")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Method name " + method + " could be named better.")
				.build());
		}
		if (name.hasPrefixOrPostfix(method)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("METHOD_HAS_PREFIX_OR_POSTFIX")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Method name has prefix or postfix " + method + " and could be named better.")
				.build());
		}
	}
}