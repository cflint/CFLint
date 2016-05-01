package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class TooManyArgumentsChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";
	final int ARGUMENT_THRESHOLD = 10;

	protected int argumentCount = 0;
	protected int functionLine = 0;

	@Override
	public void expression(final CFScriptStatement expression,
			final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			final int begLine = function.getLine();
			int noArguments = function.getFormals().size();

			checkNumberArguments(noArguments, begLine, context, bugs);
		}
	}

	@Override
	public void element(final Element element, final Context context,
			final BugList bugs) {
		if (element.getName().equals("cffunction")) {
			functionLine = element.getSource().getRow(element.getBegin());
			argumentCount = 0;
		} else if (element.getName().equals("cfargument")) {
			argumentCount++;
		}
		// No easy way of detecting end tag so assumes functions will contain
		// some code
		// otherwise the argument count will be off by one
		else if (argumentCount > 0) {
			checkNumberArguments(argumentCount, functionLine, context, bugs);
			argumentCount = 0;
			functionLine = 0;
		}
	}

	protected void checkNumberArguments(int argumentCount, int atLine,
			Context context, BugList bugs) {
		String argumentThreshold = getParameter("maximum");
		int threshold = ARGUMENT_THRESHOLD;

		if (argumentThreshold != null) {
			threshold = Integer.parseInt(argumentThreshold);
		}

		if (argumentCount > threshold) {
			bugs.add(new BugInfo.BugInfoBuilder()
					.setLine(atLine)
					.setMessageCode("EXCESSIVE_ARGUMENTS")
					.setSeverity(severity)
					.setFilename(context.getFilename())
					.setFunction(context.getFunctionName())
					.setMessage(
							"Function " + context.getFunctionName()
									+ " has too many arguments. Has"
									+ Integer.toString(argumentCount)
									+ " arguments, should be less than "
									+ Integer.toString(threshold) + ".")
					.build());
		}
	}

}
