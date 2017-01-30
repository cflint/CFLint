package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;

public class TypedQueryNew extends CFLintScannerAdapter {

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFunctionExpression) {
            final CFFunctionExpression cfFunctionExpr = (CFFunctionExpression) expression;
            if ("QueryNew".equalsIgnoreCase(cfFunctionExpr.getName()) && cfFunctionExpr.getArgs().size() == 1) {
                context.addMessage("QUERYNEW_DATATYPE", cfFunctionExpr.getName());
            }
        }
    }
}
