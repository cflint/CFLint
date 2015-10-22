package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class CreateObjectChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {	
		if (expression instanceof CFExpressionStatement) {
			String code = ((CFExpressionStatement) expression).getExpression().Decompile(0);
			int lineNo = ((CFExpressionStatement) expression).getLine() + context.startLine() - 1;
			if (code.toLowerCase().contains("createobject('component'")) {
				noNeedtoUseCreateObject(lineNo, context, bugs);
			}
		}
	}

	protected void noNeedtoUseCreateObject(final int lineNo, final Context context, final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("AVOID_USING_CREATEOBJECT")
			.setSeverity(severity).setFilename(context.getFilename())
			.setMessage("CreateObject statement at line " + lineNo + ". Use createObject(path_to_component) or even better new path_to_component().")
			.build());
	}
}