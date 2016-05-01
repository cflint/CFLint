package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class ArgTypeChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			final int begLine = function.getLine();

			for (final CFFunctionParameter argument : function.getFormals()) {
				final String name = argument.getName();
				final String variableType = argument.getType();

				if (variableType == null) {
					bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode("ARG_TYPE_MISSING")
						.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
						.setMessage("Argument " + name + " is missing a type.")
						.setVariable(name)
						.build());
				}
				else if (variableType.equals("any")) {
					bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode("ARG_TYPE_ANY")
						.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
						.setMessage("Argument " + name + " is any. Please change to be the correct type.")
						.setVariable(name)
						.build());
				}
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name");
			final String variableType = element.getAttributeValue("type");
			int begLine = element.getSource().getRow(element.getBegin());

			if (variableType == null) {
				bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode("ARG_TYPE_MISSING")
					.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
					.setMessage("Argument " + name + " is missing a type.")
					.setVariable(name)
					.build());
			}
			else if (variableType.equals("any")) {
				bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode("ARG_TYPE_ANY")
					.setSeverity(severity).setFilename(context.getFilename())
					.setFunction(context.getFunctionName())
					.setMessage("Argument " + name + " is any. Please change to be the correct type.")
					.setVariable(name)
					.build());
			}
		}
	}

}
