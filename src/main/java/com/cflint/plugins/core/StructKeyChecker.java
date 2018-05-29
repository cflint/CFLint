package com.cflint.plugins.core;

import java.util.List;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFStructElementExpression;
import cfml.parsing.cfscript.CFStructExpression;

public class StructKeyChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {

        if (context.isInAssignmentExpression() && expression instanceof CFFullVarExpression) {
            final List<CFExpression> subExpressions = expression.decomposeExpression();
            if (subExpressions != null && subExpressions.size() > 1
                    && subExpressions.get(subExpressions.size() - 1) instanceof CFIdentifier) {
                context.addMessage("STRUCT_ARRAY_NOTATION", subExpressions.get(subExpressions.size() - 1).Decompile(0));
            }
        } else if (expression instanceof CFStructExpression) {
            final CFStructExpression structExpression = (CFStructExpression) expression;
            for (final Object element : structExpression.getElements()) {
                final CFStructElementExpression structKeyExpression = (CFStructElementExpression) element;
                final String firstToken = structKeyExpression.getKey().getToken().getText();
                if (!"'".equals(firstToken) && !"\"".equals(firstToken)) {
                    context.addMessage("UNQUOTED_STRUCT_KEY", structKeyExpression.getKey().Decompile(0));
                }
            }
        }
    }

}
