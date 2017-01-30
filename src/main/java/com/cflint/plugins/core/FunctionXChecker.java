package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import ro.fortsoft.pf4j.Extension;

@Extension
public class FunctionXChecker extends CFLintScannerAdapter {
    final String severity = "INFO";

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFunctionExpression) {
            final String cfmlFunctionCheck = getParameter("functionName");

            final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
            if (functionExpression.getName().equalsIgnoreCase(cfmlFunctionCheck)) {
                // int lineNo = expression.getLine() + context.startLine() - 1;
                // structNew(lineNo, context, bugs);
                context.addMessage("AVOID_USING_" + cfmlFunctionCheck.toUpperCase(), cfmlFunctionCheck);

            }
        }
    }

}