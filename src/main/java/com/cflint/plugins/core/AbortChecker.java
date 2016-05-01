package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import cfml.parsing.cfscript.script.CFAbortStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class AbortChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";
	
	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFAbortStatement) {
			int lineNo = ((CFAbortStatement) expression).getLine() + context.startLine() - 1;
			bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("AVOID_USING_ABORT")
				.setSeverity(severity).setFilename(context.getFilename())
				.setMessage("Abort statement at line " + lineNo + ". Avoid using abort in production code.")
				.build());
		}
	}
}