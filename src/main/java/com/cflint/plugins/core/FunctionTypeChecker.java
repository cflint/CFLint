package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class FunctionTypeChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			final int begLine = function.getLine();
			final String functionType = function.getReturnType();

			checkReturnType(functionType, begLine, context, bugs);
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cffunction")) {
			final int begLine = element.getSource().getRow(element.getBegin());
			final String functionType = element.getAttributeValue("returnType");

			checkReturnType(functionType, begLine, context, bugs);
		}
	}

	protected void checkReturnType(final String functionType, final int lineNumber, final Context context, final BugList bugs) {
		if (functionType == null || functionType.length() == 0) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNumber).setMessageCode("FUNCTION_TYPE_MISSING")
				.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
				.setMessage("Function " + context.getFunctionName() + " is missing a return type.")
				.build());
		}
		else if (functionType.equals("any")) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNumber).setMessageCode("FUNCTION_TYPE_ANY")
				.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
				.setMessage("Function " + context.getFunctionName() + " return type is any. Please change to be the correct type.")
				.build());
		}
	}

}
