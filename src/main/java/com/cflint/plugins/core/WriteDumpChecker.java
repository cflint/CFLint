package com.cflint.plugins.core;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import ro.fortsoft.pf4j.Extension;

@Extension
public class WriteDumpChecker extends CFLintScannerAdapter {
	final String severity = "INFO";
	
	@Override
	public void expression(final CFExpression expression, final Context context,
			final BugList bugs) {
		
		if(expression instanceof CFFunctionExpression){
			final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
			if(functionExpression.getFunctionName().equalsIgnoreCase("writeDump")){
				final int lineNo = functionExpression.getLine() + context.startLine() - 1;
				context.addMessage("AVOID_USING_WRITEDUMP", null);
			}
		}
		
	}

}