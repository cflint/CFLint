package com.cflint.plugins.core;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class TooManyFunctionsChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";
	final int FUNCTION_THRESHOLD = 10;

	protected int functionCount = 0;
	protected boolean alreadyTooMany = false;

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFCompDeclStatement) {
			functionCount = 0;
			alreadyTooMany = false;
		}
		else if (expression instanceof CFFuncDeclStatement) {

			if (!trivalFunction(context.getFunctionName())) {
				functionCount++;

				if (!alreadyTooMany) {
					checkNumberFunctions(functionCount, 1, context, bugs);
				}
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfcomponent")) {	
			functionCount = 0;
			alreadyTooMany = false;
		}
		else if (element.getName().equals("cffunction") && !trivalFunction(context.getFunctionName())) {
			functionCount++;

			if (!alreadyTooMany) {
				checkNumberFunctions(functionCount, 1, context, bugs);
			}
		}
	}

	protected boolean trivalFunction(String name) {
		final int length = name.length();
		return length >= 3 && name.substring(1,3) == "get"
		 || length >= 3 && name.substring(1,3) == "set"
		  || length >= 2 && name.substring(1,2) == "is";
	}

	protected void checkNumberFunctions(int functionCount, int atLine, Context context, BugList bugs) {
		String functionThreshold = getParameter("maximum");
		int threshold = FUNCTION_THRESHOLD;

		if (functionThreshold != null) {
			threshold = Integer.parseInt(functionThreshold);
		}

		if (functionCount > threshold) {
			alreadyTooMany = true;
			bugs.add(new BugInfo.BugInfoBuilder().setLine(atLine).setMessageCode("EXCESSIVE_FUNCTIONS")
					.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
					.setMessage("Function " + context.getFunctionName() + " has too many functions. Should be less than " + Integer.toString(threshold) + ".")
					.build());
		}
	}

}
