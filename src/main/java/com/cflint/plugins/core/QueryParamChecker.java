package com.cflint.plugins.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFJavaMethodExpression;
import cfml.parsing.cfscript.cfFullVarExpression;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class QueryParamChecker extends CFLintScannerAdapter {

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof cfFullVarExpression) {
			final cfFullVarExpression cfFunctionExpr = (cfFullVarExpression) expression;
			final StringBuilder varName = new StringBuilder();
			for (final CFExpression expression2 : cfFunctionExpr.getExpressions()) {
				if (expression2 instanceof CFJavaMethodExpression) {
					final CFJavaMethodExpression javaexpression = (CFJavaMethodExpression) expression2;
					if (javaexpression.getFunctionName().equalsIgnoreCase("setSql")
							&& javaexpression.getArguments().size() > 0) {
						final CFExpression argsExpression = javaexpression.getArguments().get(0);
						if (argsExpression instanceof CFBinaryExpression) {
							context.addMessage("QUERYPARAM_REQ", varName.toString());
						}
					}
				} else {
					if (varName.length() > 0) {
						varName.append(".");
					}
					varName.append(expression2.Decompile(0));
				}
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (// element.getName().equals("cfcomponent") ||
		element.getName().equals("cfquery") && ! "query".equalsIgnoreCase(element.getAttributeValue("dbtype"))) {
			final String content = element.getTextExtractor().toString();
			if (content.indexOf("#") > 0) {
				final Pattern pattern = Pattern.compile("#(.+?)#");
				final Matcher matcher = pattern.matcher(content);
				while (matcher.find()) {
					if (matcher.groupCount() >= 1) {
						final String variableName = matcher.group(1);
						context.addMessage("CFQUERYPARAM_REQ", variableName);
					}
				}
			}
		}
	}

}
