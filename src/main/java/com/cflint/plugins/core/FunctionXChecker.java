package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import ro.fortsoft.pf4j.Extension;

/**
 * Avoid using function X checker.
 */
@Extension
public class FunctionXChecker extends CFLintScannerAdapter {

    /**
     * Check an CF expression for use of function X.
     *
     * @param expression expresison to check.
     * @param context expresion context.
     * @param bugs list of bugs.
     */
    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFunctionExpression) {
            final String cfmlFunctionCheck = context.getConfiguration().getParameter(this,"functionName");

            final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
            if (functionExpression.getName().equalsIgnoreCase(cfmlFunctionCheck)) {
                context.addMessage("AVOID_USING_" + cfmlFunctionCheck.toUpperCase(), cfmlFunctionCheck);

            }
        }
    }

}
