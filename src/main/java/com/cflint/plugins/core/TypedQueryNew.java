package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;

public class TypedQueryNew implements CFLintScanner {

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFunctionExpression) {
			final CFFunctionExpression cfFunctionExpr = (CFFunctionExpression) expression;
			if ("QueryNew".equalsIgnoreCase(cfFunctionExpr.getName()) && cfFunctionExpr.getArgs().size() == 1) {
				bugs.add(new BugInfo.BugInfoBuilder().setMessageCode("QUERYNEW_DATATYPE")
						.setMessage("QueryNew statement should specify datatypes.").setVariable("QueryNew")
						.setFunction(context.getFunctionName()).setSeverity("WARNING")
						.setFilename(context.getFilename()).setExpression(expression.Decompile(0))
						.build(expression, context.getElement()));
			}
		}
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		// TODO Auto-generated method stub

	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		
	}

}
