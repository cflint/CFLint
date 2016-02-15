package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.script.CFIfStatement;
import cfml.parsing.cfscript.script.CFForStatement;
import cfml.parsing.cfscript.script.CFForInStatement;
import cfml.parsing.cfscript.script.CFSwitchStatement;
import cfml.parsing.cfscript.script.CFTryCatchStatement;
import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.CFWhileStatement;
import cfml.parsing.cfscript.script.CFDoWhileStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

public class SimpleComplexityChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";
	final protected int COMPLEXITY_THRESHOLD = 10;

	protected int complexity = 0;
	protected boolean alreadyTooComplex = false;
	int functionLineNo = 1;

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		CFFuncDeclStatement function = null;

		if (expression instanceof CFFuncDeclStatement) {
			function = (CFFuncDeclStatement) expression;
			functionLineNo = function.getLine();
			complexity = 0;
			alreadyTooComplex = false;
		}
		// Not using instanceof to avoid double counting
		else if (expression.getClass().equals(CFIfStatement.class) ||
			expression.getClass().equals(CFForStatement.class) ||
			expression.getClass().equals(CFForInStatement.class) ||
			expression.getClass().equals(CFSwitchStatement.class) ||
			expression.getClass().equals(CFTryCatchStatement.class) ||
			expression.getClass().equals(CFWhileStatement.class) ||
			expression.getClass().equals(CFDoWhileStatement.class)) {
			complexity++;
			// TODO +1 for each case statment in a switch
			checkComplexity(context.getFunctionName(), functionLineNo, context, bugs);
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		final String name = element.getName();

		if (name.equalsIgnoreCase("cffunction")) {
			functionLineNo = element.getSource().getRow(element.getBegin());
			complexity = 0;
			alreadyTooComplex = false;
		}
		else {
			if (name.equalsIgnoreCase("cfif") || name.equalsIgnoreCase("cfelse") || name.equalsIgnoreCase("cfelseif")
				|| name.equalsIgnoreCase("cfloop")  || name.equalsIgnoreCase("cfwhile") || name.equalsIgnoreCase("cfoutput") // TODO could check for query=
				|| name.equalsIgnoreCase("cfcase") || name.equalsIgnoreCase("cfdefaultcase")
				|| name.equalsIgnoreCase("cftry") || name.equalsIgnoreCase("cfcatch")) {
				complexity++;
				checkComplexity(context.getFunctionName(), functionLineNo, context, bugs);
			}
		}
	}

	protected void checkComplexity(String name, int lineNo, Context context, BugList bugs) {
		String complexityThreshold = getParameter("maximum");
		int threshold = COMPLEXITY_THRESHOLD;

		if (complexityThreshold != null) {
			threshold = Integer.parseInt(complexityThreshold);
		}

		if (!alreadyTooComplex && complexity > threshold) {
			alreadyTooComplex = true;

			bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("FUNCTION_TOO_COMPLEX")
				.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
				.setMessage("Function " + name + " is too complex. Consider breaking the function into smaller functions.")
				.build());
		}
	}

}
