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

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		CFFuncDeclStatement function = null;
		int begLine = 1;

		if (expression instanceof CFFuncDeclStatement) {
			function = (CFFuncDeclStatement) expression;
			begLine = function.getLine();
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
			checkComplexity(context.getFunctionName(), begLine, context, bugs);
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		final String name = element.getName();
		int begLine = 1;

		if (name.equals("cffunction")) {
			begLine = element.getSource().getRow(element.getBegin());
			complexity = 0;
			alreadyTooComplex = false;
		}
		else {
			if (name.equals("cfif") || name.equals("cfelse") || name.equals("cfelseif")
				|| name.equals("cfloop")  || name.equals("cfwhile") || name.equals("cfoutput") // TODO could check for query=
				|| name.equals("cfcase") || name.equals("cfdefaultcase")
				|| name.equals("cftry") || name.equals("cfcatch")) {
				complexity++;
				checkComplexity(context.getFunctionName(), begLine, context, bugs);
			}
		}
	}

	protected void checkComplexity(String name, int lineno, Context context, BugList bugs) {
		String complexityThreshold = getParameter("maximum");
		int threshold = COMPLEXITY_THRESHOLD;

		if (complexityThreshold != null) {
			threshold = Integer.parseInt(complexityThreshold);
		}
		
		if (!alreadyTooComplex && complexity > threshold) {
			alreadyTooComplex = true;

			bugs.add(new BugInfo.BugInfoBuilder().setLine(lineno).setMessageCode("FUNCTION_TOO_COMPLEX")
				.setSeverity(severity).setFilename(context.getFilename())
				.setMessage("Function " + name + " is has a name attribute perhaps you meant to use displayName?")
				.build());
		}
	}

}
