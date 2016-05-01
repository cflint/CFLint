package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class WriteDumpChecker extends CFLintScannerAdapter {
	final String severity = "INFO";
	
	@Override
	public void expression(final CFExpression expression, final Context context,
			final BugList bugs) {
		
		if(expression instanceof CFFunctionExpression){
			final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
			if(functionExpression.getFunctionName().equals("writeDump")){
				final int lineNo = functionExpression.getLine() + context.startLine() - 1;
				writeDump(lineNo, context, bugs);
			}
		}
		
	}

	protected void writeDump(final int lineNo, final Context context, final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("AVOID_USING_WRITEDUMP")
			.setSeverity(severity).setFilename(context.getFilename())
			.setMessage("WriteDump statement at line " + lineNo + ". Avoid using writeDump in production code.")
			.build());
	}
}