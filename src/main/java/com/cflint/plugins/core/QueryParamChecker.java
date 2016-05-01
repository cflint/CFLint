package com.cflint.plugins.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class QueryParamChecker extends CFLintScannerAdapter {

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFunctionExpression) {
			final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
			if (functionExpression.getFunctionName().equalsIgnoreCase("setSql")
					&& functionExpression.getArgs().size() > 0) {
				final CFExpression argsExpression = functionExpression.getArgs().get(0);
				Pattern p = Pattern.compile(".*#[^#].*",Pattern.DOTALL);
				if (p.matcher(argsExpression.Decompile(0)).matches()) {
					context.addMessage("QUERYPARAM_REQ", functionExpression.getName());
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
						context.addMessage("CFQUERYPARAM_REQ", element.getAttributeValue("NAME"));
					}
				}
			}
		}
	}

}
