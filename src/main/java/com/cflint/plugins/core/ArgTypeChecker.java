package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class ArgTypeChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;

            for (final CFFunctionParameter argument : function.getFormals()) {
                final String name = argument.getName();
                final String variableType = argument.getType();

                if (variableType == null) {
                    context.addMessage("ARG_TYPE_MISSING", name);
                } else if ("any".equals(variableType)) {
                    context.addMessage("ARG_TYPE_ANY", name);
                }
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFARGUMENT)) {
            final String name = element.getAttributeValue(CF.NAME);
            final String variableType = element.getAttributeValue(CF.TYPE);

            if (variableType == null) {
                context.addMessage("ARG_TYPE_MISSING", name);
            } else if ("any".equals(variableType)) {
                context.addMessage("ARG_TYPE_ANY", name);
            }
        }
    }

}
