package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class ArgDefChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            for (final CFFunctionParameter argument : function.getFormals()) {
                final String name = argument.getName();
                if (!argument.toString().contains(CF.REQUIRED) && !argument.toString().contains("=")) {
                    context.addMessage("ARG_DEFAULT_MISSING", name);
                }
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFARGUMENT)) {
            final String name = element.getAttributeValue(CF.NAME);
            final boolean required = CFTool.toBoolean(element.getAttributeValue(CF.REQUIRED));
            final String defaultExpr = element.getAttributeValue(CF.DEFAULT);
            if (!required && defaultExpr == null) {
                element.getSource().getRow(element.getBegin());
                element.getSource().getColumn(element.getBegin());
                context.addMessage("ARG_DEFAULT_MISSING", name);
            }
        }
    }

}
