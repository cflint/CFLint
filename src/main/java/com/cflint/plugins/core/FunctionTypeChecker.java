package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class FunctionTypeChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            final int begLine = function.getLine();
            final String functionType = function.getReturnType() == null ? null : function.getReturnType().Decompile(0);

            checkReturnType(functionType, begLine, context, bugs);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFFUNCTION)) {
            final int begLine = element.getSource().getRow(element.getBegin());
            final String functionType = element.getAttributeValue("returnType");

            checkReturnType(functionType, begLine, context, bugs);
        }
    }

    protected void checkReturnType(final String functionType, final int lineNumber, final Context context,
            final BugList bugs) {
        if (functionType == null || functionType.length() == 0) {
            context.addMessage("FUNCTION_TYPE_MISSING", context.getFunctionName());
        } else if ("any".equals(functionType)) {
            context.addMessage("FUNCTION_TYPE_ANY", context.getFunctionName());
        }
    }

}
