package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class ArgDefChecker extends CFLintScannerAdapter {

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			for (final CFFunctionParameter argument : function.getFormals()) {
				// handler.addArgument(param.getName());
				final String name = argument.getName();
				if (!argument.toString().contains("required") && !argument.toString().contains("=")) {
					context.addMessage("ARG_DEFAULT_MISSING", name);
				}
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name");
			final boolean required = CFTool.toBoolean(element.getAttributeValue("required"));
			final String defaultExpr = element.getAttributeValue("default");
			if (!required && defaultExpr == null) {
				element.getSource().getRow(element.getBegin());
				element.getSource().getColumn(element.getBegin());
				context.addMessage("ARG_DEFAULT_MISSING", name);
			}
		}
	}

}
