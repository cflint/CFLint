package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import ro.fortsoft.pf4j.Extension;

@Extension
public class CreateObjectChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFExpressionStatement) {
            final String code = ((CFExpressionStatement) expression).getExpression().Decompile(0);
            final int lineNo = ((CFExpressionStatement) expression).getLine() + context.startLine() - 1;
            if (code.toLowerCase().contains("createobject('component'")) {
                noNeedtoUseCreateObject(lineNo, context);
            }
        }
    }

    protected void noNeedtoUseCreateObject(final int lineNo, final Context context) {
        context.addMessage("AVOID_USING_CREATEOBJECT", null, lineNo);
    }
}