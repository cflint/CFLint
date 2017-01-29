package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class ArgumentNameChecker extends CFLintScannerAdapter {

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			final int lineNo = function.getLine() + context.startLine() - 1;

			for (final CFFunctionParameter argument : function.getFormals()) {
				checkNameForBugs(context,argument.getName(), context.getFilename(), context.getFunctionName(), lineNo, bugs);
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final int lineNo = context.startLine();
			final String name = element.getAttributeValue("name");
			if (name != null && name.length() > 0) {
				checkNameForBugs(context,name, context.getFilename(), context.getFunctionName(), lineNo, bugs);
			} else {
				context.addMessage("ARGUMENT_MISSING_NAME", null, this, lineNo);
			}
		}
	}

	public void checkNameForBugs(final Context context, final String argument, final String filename, final String functionName,
			final int line, final BugList bugs) {
		int minArgLength = ValidName.MIN_ARGUMENT_LENGTH;
		int maxArgLength = ValidName.MAX_ARGUMENT_LENGTH;
		int maxArgWords = ValidName.MAX_ARGUMENT_WORDS;

		if (getParameter("MinLength") != null) {
			try {
				minArgLength = Integer.parseInt(getParameter("MinLength"));
			} catch (final Exception e) {
			}
		}

		if (getParameter("MaxLength") != null) {
			try {
				maxArgLength = Integer.parseInt(getParameter("MaxLength"));
			} catch (final Exception e) {
			}
		}

		if (getParameter("MaxWords") != null) {
			try {
				maxArgWords = Integer.parseInt(getParameter("MaxWords"));
			} catch (final Exception e) {
			}
		}

		final ValidName name = new ValidName(minArgLength, maxArgLength, maxArgWords);

		if (name.isInvalid(argument)) {
		     context.addMessage("ARGUMENT_INVALID_NAME", null, this, line);
		}
		if (name.isUpperCase(argument)) {
		    context.addMessage("ARGUMENT_ALLCAPS_NAME", argument, this, line);
		}
		if (name.tooShort(argument)) {
            context.addMessage("ARGUMENT_TOO_SHORT", argument, this, line);
		}
		if (name.tooLong(argument)) {
            context.addMessage("ARGUMENT_TOO_LONG", argument, this, line);
		}
		if (!name.isUpperCase(argument) && name.tooWordy(argument)) {
            context.addMessage("ARGUMENT_TOO_WORDY", argument, this, line);
		}
		if (name.isTemporary(argument)) {
            context.addMessage("ARGUMENT_IS_TEMPORARY", argument, this, line);
		}
		if (name.hasPrefixOrPostfix(argument)) {
            context.addMessage("ARGUMENT_HAS_PREFIX_OR_POSTFIX", argument, this, line);
		}
	}
}