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
            int offset = CFScopes.isScoped((CFFullVarExpression)expression)?2:1;
            if (subExpressions != null && subExpressions.size() > offset 
                    && subExpressions.get(subExpressions.size() - offset) instanceof CFIdentifier) {
                context.addMessage("STRUCT_ARRAY_NOTATION", subExpressions.get(subExpressions.size() - offset).Decompile(0),this,null,null,subExpressions.get(subExpressions.size() - offset));
            }
        }
        else if (expression instanceof CFStructExpression) {
            CFStructExpression structExpression = (CFStructExpression) expression;
            for (Object element : structExpression.getElements()) {
                CFStructElementExpression structKeyExpression = (CFStructElementExpression) element;
                String firstToken = structKeyExpression.getKey().getToken().getText();
                if (!"'".equals(firstToken) && !"\"".equals(firstToken)) {
                    context.addMessage("UNQUOTED_STRUCT_KEY", structKeyExpression.getKey().Decompile(0),this,null,null,structKeyExpression.getKey());
                }
            }
        }
    }

}
