package com.cflint.plugins.core;

import java.util.Map;
import java.util.HashMap;

import ro.fortsoft.pf4j.Extension;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFLiteral;
import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;


import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class LiteralChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";
	final protected int REPEAT_THRESHOLD = 3;
	final protected int WARNING_THRESHOLD = 5;
	
	protected int threshold = REPEAT_THRESHOLD;
	protected int warningThreshold = WARNING_THRESHOLD;

	protected Map<String, Integer> globalLiterals = new HashMap<String, Integer>();
	protected Map<String, Integer> functionListerals = new HashMap<String, Integer>();

	// May want to consider resetting literal map on new components but this way it
	// detects duplicated literals across files which is useful

	@Override	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		String repeatThreshold = getParameter("maximum");
		String maxWarnings = getParameter("maxWarnings");
		String warningScope = getParameter("warningScope");

		if (repeatThreshold != null) {
			threshold = Integer.parseInt(repeatThreshold);
		}

		if (maxWarnings != null) {
			warningThreshold = Integer.parseInt(maxWarnings);
		}

		if (expression instanceof CFLiteral) {
			CFLiteral literal = (CFLiteral) expression;
			String name = literal.Decompile(0).replace("'","");

			if (isCommon(name)) {
				return;
			}

			int lineNo = literal.getLine() + context.startLine() - 1;

			if (warningScope == null || warningScope.equals("global")) {
				literalCount(name, lineNo, globalLiterals, true, context, bugs);
			}
			else if (warningScope.equals("local")) {
				literalCount(name, lineNo, functionListerals, false, context, bugs);
			}
		}
	}

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFCompDeclStatement) {
			functionListerals.clear();
		}
	}

	protected void literalCount(final String name, final int lineNo, Map<String, Integer> literals, boolean global, final Context context, final BugList bugs) {
		int count = 1;

		if (literals.get(name) == null) {
			literals.put(name, count);
		}
		else {
			count = literals.get(name);
			count++;
			literals.put(name, count);
		}

		if (count > threshold && (warningThreshold == -1 || (count - threshold) <= warningThreshold)) {
			if (global) {
				magicGlobalValue(name, lineNo, context, bugs);
			}
			else {
				magicLocalValue(name, lineNo, context, bugs);
			}
		}
	}

	protected boolean isCommon(final String name) {
		return name.equals("1") || name.equals("0") || name.equals("") || name.equals("true") || name.equals("false");
	}

	public void magicLocalValue(final String name, final int lineNo, final Context context, final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("LOCAL_LITERAL_VALUE_USED_TOO_OFTEN")
			.setSeverity(severity).setFilename(context.getFilename())
			.setMessage("Literal " + name + " occurs several times in the same file. Consider giving it a name and not hard coding values.")
			.build());
	}

	public void magicGlobalValue(final String name, final int lineNo, final Context context, final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN")
			.setSeverity(severity).setFilename(context.getFilename())
			.setMessage("Literal " + name + " occurs several times in one or more files. Consider giving it a name and not hard coding values.")
			.build());
	}
}