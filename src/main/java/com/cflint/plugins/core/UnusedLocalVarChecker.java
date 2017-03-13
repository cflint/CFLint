package com.cflint.plugins.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFMember;
import cfml.parsing.cfscript.CFVarDeclExpression;

public class UnusedLocalVarChecker extends CFLintScannerAdapter {
    final String severity = "INFO";

    protected CFScopes scopes = new CFScopes();
    // LinkedHashMap is ordered.
    protected Map<String, Boolean> localVariables = new LinkedHashMap<String, Boolean>();
    protected Map<String, Integer> variableLineNo = new HashMap<String, Integer>();

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
    	System.out.println(expression);
    	System.out.println(expression.Decompile(0));
        if (expression instanceof CFFullVarExpression) {
            final CFFullVarExpression fullVarExpression = (CFFullVarExpression) expression;
            final CFExpression variable = fullVarExpression.getExpressions().get(0);
            if (variable instanceof CFIdentifier) {
                final String name = ((CFIdentifier) variable).getName();
                if (!scopes.isCFScoped(name)) {
                    localVariables.put(name, true);
                } else if (scopes.isLocalScoped(name) && fullVarExpression.getExpressions().size() > 1) {
                    final CFExpression variable2 = fullVarExpression.getExpressions().get(1);
                    if (variable2 instanceof CFIdentifier) {
                        localVariables.put(((CFIdentifier) variable2).getName(), true);
                    }
                }
            }
            for (CFExpression subexpr : ((CFFullVarExpression) expression).getExpressions()) {
                if (subexpr instanceof CFMember) {
                    CFMember memberExpr = (CFMember) subexpr;
                    if (memberExpr.getExpression() != null) {
                        expression(memberExpr.getExpression(), context, bugs);
                    }
                }
            }
        } else if (expression instanceof CFVarDeclExpression) {
            final String name = ((CFVarDeclExpression) expression).getName();
            final int lineNo = expression.getLine() + context.startLine() - 1;
            addLocalVariable(name, lineNo);
        } else if (expression instanceof CFIdentifier) {
            localVariables.put(((CFIdentifier) expression).getName(), true);
        }
    }
    /*
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            final String decompile = function.Decompile(1);
            final int begLine = function.getLine();
            final String[] lines = decompile.split("\\n");

            checkSize(context, begLine, lines.length, bugs);
        }
    }
    */

    protected void addLocalVariable(final String variable, final Integer lineNo) {
        if (localVariables.get(variable) == null) {
            localVariables.put(variable, false);
            setLocalVariableLineNo(variable, lineNo);
        }
    }

    protected void setLocalVariableLineNo(final String variable, final Integer lineNo) {
        if (variableLineNo.get(variable) == null) {
            variableLineNo.put(variable, lineNo);
        }
    }

    @Override
    public void startFunction(final Context context, final BugList bugs) {
        localVariables.clear();
        variableLineNo.clear();
    }

    @Override
    public void endFunction(final Context context, final BugList bugs) {
        // sort by line number
        for (final Map.Entry<String, Boolean> variable : localVariables.entrySet()) {
            final Boolean used = variable.getValue();
            if (!used) {
                final String name = variable.getKey();
                final Integer lineNo = variableLineNo.get(name);
                context.addMessage("UNUSED_LOCAL_VARIABLE", name, lineNo);
            }
        }
    }

}
