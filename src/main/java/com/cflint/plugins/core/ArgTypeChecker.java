package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class ArgTypeChecker extends CFLintScannerAdapter {

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;

			for (final CFFunctionParameter argument : function.getFormals()) {
				final String name = argument.getName();
				final String variableType = argument.getType();

				if (variableType == null) {
					context.addMessage("ARG_TYPE_MISSING", name);
				}
				else if (variableType.equals("any")) {
					context.addMessage("ARG_TYPE_ANY", name);
				}
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name");
			final String variableType = element.getAttributeValue("type");

			if (variableType == null) {
				context.addMessage("ARG_TYPE_MISSING", name);
			}
			else if (variableType.equals("any")) {
				context.addMessage("ARG_TYPE_ANY", name);
			}
		}
	}

}