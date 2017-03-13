package com.cflint.plugins.core;

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
    protected Map<String, VarInfo> localVariables = new LinkedHashMap<String, VarInfo>();
    //protected Map<String, Integer> variableLineNo = new HashMap<String, Integer>();

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
                    localVariables.put(name.toLowerCase(), new VarInfo(name, true));
                } else if (scopes.isLocalScoped(name) && fullVarExpression.getExpressions().size() > 1) {
                    final CFExpression variable2 = fullVarExpression.getExpressions().get(1);
                    if (variable2 instanceof CFIdentifier) {
                        final String namepart=((CFIdentifier) variable2).getName();
                        localVariables.put(namepart.toLowerCase(), new VarInfo(namepart, true));
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
            final String name = ((CFIdentifier) expression).getName();
            if(name != null){
                localVariables.put(name.toLowerCase(), new VarInfo(name, true));
            }
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
        if (variable != null && localVariables.get(variable.toLowerCase()) == null) {
            localVariables.put(variable.toLowerCase(), new VarInfo(variable, false));
            setLocalVariableLineNo(variable, lineNo);
        }
    }

    protected void setLocalVariableLineNo(final String variable, final Integer lineNo) {
        if (variable != null && localVariables.get(variable.toLowerCase()) != null) {
            localVariables.get(variable).lineNumber=lineNo;
        }
    }

    @Override
    public void startFunction(final Context context, final BugList bugs) {
        localVariables.clear();    }

    @Override
    public void endFunction(final Context context, final BugList bugs) {
        // sort by line number
        for (final VarInfo variable : localVariables.values()) {
            final Boolean used = variable.used;
            if (!used) {
                final String name = variable.name;
                final Integer lineNo = variable.lineNumber;
                context.addMessage("UNUSED_LOCAL_VARIABLE", name, lineNo);
            }
        }
    }

    public static class VarInfo{
        
        public VarInfo(String name, Boolean used){
            this.name=name;
            this.used=used;
        }
        Boolean used;
        Integer lineNumber;
        String name;
    }
}
