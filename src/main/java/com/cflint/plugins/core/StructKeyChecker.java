package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFStructElementExpression;
import cfml.parsing.cfscript.CFStructExpression;

public class StructKeyChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {

        if (expression instanceof CFStructExpression) {
            CFStructExpression structExpression = (CFStructExpression) expression;
            for (Object element : structExpression.getElements()) {
                CFStructElementExpression structKeyExpression = (CFStructElementExpression) element;
                String firstToken = structKeyExpression.getKey().getToken().getText();
                if (!"'".equals(firstToken) && !"\"".equals(firstToken)) {
                    context.addMessage("UNQUOTED_STRUCT_KEY", structKeyExpression.getKey().Decompile(0));
                }
            }
        }
    }

}
