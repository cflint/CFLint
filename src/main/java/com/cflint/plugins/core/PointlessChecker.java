package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFIfStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;

public class PointlessChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression != null) {
            if (expression.getClass().equals(CFIfStatement.class) ) {
                final CFIfStatement ifStatement = (CFIfStatement) expression;
                if ( ifStatement.getThenStatement() == null && ifStatement.getElseStatement() == null ) {
                    context.addMessage("POINTLESS_CHECK", null);
                }
            }
        }
    }

}
