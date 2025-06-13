package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.UserDefinedFunction;
import net.htmlparser.jericho.Element;

public class FunctionTypeChecker extends CFLintScannerAdapter {

    
    /** 
     * @param expression expression
     * @param context context
     * @param bugs bugs
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            final int begLine = function.getLine();
            final String functionType = function.getReturnType() == null ? null : function.getReturnType().Decompile(0);
            final boolean remote = function.getAccess() == UserDefinedFunction.ACCESS_REMOTE ? true : false;

            checkReturnType(functionType, remote, begLine, context, bugs);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFFUNCTION)) {
            final int begLine = element.getSource().getRow(element.getBegin());
            final String functionType = element.getAttributeValue("returnType");
            final String access = element.getAttributeValue("access");
            final boolean remote = access != null && access.equals("remote") ? true : false;

            checkReturnType(functionType, remote, begLine, context, bugs);
        }
    }

    protected void checkReturnType(final String functionType, final boolean remote, final int lineNumber, final Context context,
            final BugList bugs) {
        if (functionType == null || functionType.length() == 0) {
            context.addMessage("FUNCTION_TYPE_MISSING", context.getFunctionName());
        } else if ("any".equals(functionType)) {
            context.addMessage("FUNCTION_TYPE_ANY", context.getFunctionName());
        }
        if (remote == true) {
            context.addMessage("FUNCTION_ACCESS_REMOTE", context.getFunctionName());
        }
    }

}
