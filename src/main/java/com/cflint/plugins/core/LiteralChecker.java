package com.cflint.plugins.core;

import java.util.Map;
import java.util.HashMap;

import ro.fortsoft.pf4j.Extension;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFLiteral;
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

	protected int threshold = 3;
	protected Map<String, Integer> literals = new HashMap<String, Integer>();
	protected Map<String, Boolean> done = new HashMap<String, Boolean>();

	// May want to consider resetting literal map on new components but this way it
	// detects duplicated literals across files which is useful

	@Override	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		String repeatThreshold = getParameter("maximum");
		int threshold = REPEAT_THRESHOLD;

		if (repeatThreshold != null) {
			threshold = Integer.parseInt(repeatThreshold);
		}

		if (expression instanceof CFLiteral) {
			CFLiteral literal = (CFLiteral) expression;
			String name = literal.Decompile(0).replace("'","");
			int count = 1;

			if (isCommon(name)) {
				return;
			}

			if (literals.get(name) == null) {
				literals.put(name, count);
				done.put(name, false);
			}
			else {
				count = literals.get(name);
				count++;
				literals.put(name, count);
			}

			if (count > threshold && !done.get(name)) {
				int lineNo = literal.getLine() + context.startLine() - 1;
				magicValue(name, lineNo, context, bugs);
				done.put(name, true);
			}
		}
	}

	protected boolean isCommon(final String name) {
		return name.equals("1") || name.equals("0") || name.equals("") || name.equals("true") || name.equals("false");
	}

	public void magicValue(final String name, final int lineNo, final Context context, final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("LITERAL_VALUE_USED_TOO_OFTEN")
			.setSeverity(severity).setFilename(context.getFilename())
			.setMessage("Literal " + name + " occurs several times. Consider giving it a name and not hard coding values.")
			.build());
	}
}