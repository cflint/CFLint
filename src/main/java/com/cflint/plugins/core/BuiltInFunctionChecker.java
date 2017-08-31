package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import ro.fortsoft.pf4j.Extension;

/**
 * isDate() is too permissive. Avoid it.
 */
@Extension
public class BuiltInFunctionChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFunctionExpression) {
            final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
            if ("isDate".equalsIgnoreCase(functionExpression.getName())) {
                context.addMessage("AVOID_USING_ISDATE", null);
            }
        }
    }
}
