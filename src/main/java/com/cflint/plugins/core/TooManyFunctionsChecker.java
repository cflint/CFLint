package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

public class TooManyFunctionsChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";
	final int FUNCTION_THRESHOLD = 10;

	protected int functionCount = 0;
	protected boolean alreadyTooMany = false;

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;

			functionCount++;

			if (!alreadyTooMany) {
				checkNumberFunctions(functionCount, 1, context, bugs);
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cffunction")) {
			functionCount++;
		}

		if (!alreadyTooMany) {
			checkNumberFunctions(functionCount, 1, context, bugs);
		}
	}

	protected void checkNumberFunctions(int functionCount, int atLine, Context context, BugList bugs) {
		String functionThreshold = getParameter("maximum");
		int threshold = FUNCTION_THRESHOLD;

		if (functionThreshold != null) {
			threshold = Integer.parseInt(functionThreshold);
		}

		if (functionCount > threshold) {
			System.out.println("BUG!");
			alreadyTooMany = true;
			bugs.add(new BugInfo.BugInfoBuilder().setLine(atLine).setMessageCode("EXCESSIVE_FUNCTIONS")
					.setSeverity(severity).setFilename(context.getFilename())
					.setMessage("Function " + context.getFunctionName() + " has too many functions. Should be less than " + Integer.toString(threshold) + ".")
					.build());
		}
	}

}
