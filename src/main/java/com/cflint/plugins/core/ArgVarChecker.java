package com.cflint.plugins.core;

import java.util.HashSet;
import java.util.Set;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFVarDeclExpression;

public class ArgVarChecker extends CFLintScannerAdapter {

    /**
     * Report each occurrence once per file/function
     */
    private final Set<String> alreadyReportedExpression = new HashSet<>();
    private final Set<String> alreadyReportedFullExpression = new HashSet<>();

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFVarDeclExpression) {
            final String name = ((CFVarDeclExpression) expression).getName();
            if (context.isInFunction() && context.getCallStack().hasArgument(name)) {
                checkExpression(context, name, expression);
            }
        } else if (expression instanceof CFFullVarExpression) {
            final CFFullVarExpression fullVarExpr = (CFFullVarExpression) expression;
            if (checkFullExpression(context, fullVarExpr)) {
                return;
            }
            expression(fullVarExpr.getExpressions().get(0), context, bugs);
        }
    }

    private boolean checkFullExpression(final Context context, final CFFullVarExpression fullVarExpr) {
        if (fullVarExpr.getExpressions().size() > 1 && fullVarExpr.getExpressions().get(0) instanceof CFIdentifier) {
            final CFIdentifier cfIdentifier1 = (CFIdentifier) fullVarExpr.getExpressions().get(0);
            if ("arguments".equalsIgnoreCase(cfIdentifier1.getName())
                    && fullVarExpr.getExpressions().get(1) instanceof CFIdentifier) {
                final CFIdentifier cfIdentifier2 = (CFIdentifier) fullVarExpr.getExpressions().get(1);
                final String name = cfIdentifier2.getName();
                if (context.getCallStack().isVariable(name)) {
                    final String fileKey = context.fileFunctionString();
                    if (alreadyReportedFullExpression.contains(fileKey)) {
                        return true;
                    } else {
                        alreadyReportedFullExpression.add(fileKey);
                    }
                    context.messageBuilder(this).at(cfIdentifier1).build("ARG_VAR_MIXED", name);
                }
            }
        }
        return false;
    }

    private void checkExpression(final Context context, final String name, final CFExpression expression) {
        final String fileKey = context.fileFunctionString();
        if (alreadyReportedExpression.contains(fileKey)) {
            return;
        } else {
            alreadyReportedExpression.add(fileKey);
        }
        context.messageBuilder(this).at(expression).build("ARG_VAR_CONFLICT", name);
    }

}
