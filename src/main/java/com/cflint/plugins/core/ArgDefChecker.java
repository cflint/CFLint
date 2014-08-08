package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

@Extension
public class ArgDefChecker implements CFLintScanner {

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {

	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			for (final CFFunctionParameter argument : function.getFormals()) {
				// handler.addArgument(param.getName());
				final String name = argument.getName();
				if (!argument.toString().contains("required") && !argument.toString().contains("=")) {
					final int line = function.getLine();
					final int column = function.getColumn();

					bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setColumn(column)
							.setMessageCode("ARG_DEFAULT_MISSING").setSeverity("WARNING")
							.setFilename(context.getFilename()).setFunction(context.getFunctionName())
							.setVariable(name)
							.setMessage("Argument " + name + " is not required and does not define a default value.")
							.build());
				}
			}
		}
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name");
			final boolean required = CFTool.toBoolean(element.getAttributeValue("required"));
			final String defaultExpr = element.getAttributeValue("default");
			if (!required && defaultExpr == null) {
				final int line = element.getSource().getRow(element.getBegin());
				final int column = element.getSource().getColumn(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setColumn(column)
						.setMessageCode("ARG_DEFAULT_MISSING").setSeverity("WARNING")
						.setFilename(context.getFilename()).setFunction(context.getFunctionName()).setVariable(name)
						.setMessage("Argument " + name + " is not required and does not define a default value.")
						.build());
			}
		}
	}

}
