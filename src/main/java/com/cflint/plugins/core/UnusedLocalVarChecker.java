package com.cflint.plugins.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFMember;
import cfml.parsing.cfscript.CFVarDeclExpression;
import net.htmlparser.jericho.Element;

public class UnusedLocalVarChecker extends CFLintScannerAdapter {
    protected CFScopes scopes = new CFScopes();
    // LinkedHashMap is ordered.
    protected Map<String, VarInfo> localVariables = new LinkedHashMap<>();

    protected List<String> usedTagAttributes = null;

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFullVarExpression) {
            checkFullExpression((CFFullVarExpression) expression, context, bugs);
        } else if (expression instanceof CFVarDeclExpression) {
            checkExpression(expression, context);
        } else if (expression instanceof CFIdentifier && !context.isInAssignmentExpression()) {
            final String name = ((CFIdentifier) expression).getName();
            if (name != null) {
                localVariables.put(name.toLowerCase(), new VarInfo(name, true));
            }
        }
    }

    private void checkExpression(final CFExpression expression, final Context context) {
        final String name = ((CFVarDeclExpression) expression).getName();
        final int lineNo = expression.getLine() + context.startLine() - 1;
        if (!scopes.isCFScoped(name)) {
            addLocalVariable(name, lineNo);
        }
    }

    private void checkFullExpression(final CFFullVarExpression expression, final Context context, final BugList bugs) {
        final CFExpression variable = expression.getExpressions().get(0);
        if (variable instanceof CFIdentifier) {
            checkIdentifier(expression, (CFIdentifier) variable);
        }
        for (CFExpression subexpr : expression.getExpressions()) {
            if (subexpr instanceof CFMember) {
                CFMember memberExpr = (CFMember) subexpr;
                if (memberExpr.getExpression() != null) {
                    expression(memberExpr.getExpression(), context, bugs);
                }
            }
        }
    }

    private void checkIdentifier(final CFFullVarExpression fullVarExpression, final CFIdentifier variable) {
        final String name = variable.getName();
        if (!scopes.isCFScoped(name)) {
            localVariables.put(name.toLowerCase(), new VarInfo(name, true));
        } else if ((scopes.isLocalScoped(name) || scopes.isVariablesScoped(name)) && fullVarExpression.getExpressions().size() > 1) {
            final CFExpression variable2 = fullVarExpression.getExpressions().get(1);
            if (variable2 instanceof CFIdentifier) {
                final String namepart = ((CFIdentifier) variable2).getName();
                localVariables.put(namepart.toLowerCase(), new VarInfo(namepart, true));
            }
        }
    }

    protected void addLocalVariable(final String variable, final Integer lineNo) {
        if (variable != null && localVariables.get(variable.toLowerCase()) == null) {
            localVariables.put(variable.toLowerCase(), new VarInfo(variable, false));
            setLocalVariableLineNo(variable, lineNo);
        }
    }

    protected void setLocalVariableLineNo(final String variable, final Integer lineNo) {
        if (variable != null && localVariables.get(variable.toLowerCase()) != null) {
            localVariables.get(variable.toLowerCase()).lineNumber = lineNo;
        }
    }

    @Override
    public void startFunction(final Context context, final BugList bugs) {
        localVariables.clear();
    }

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

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        try {
            checkAttributes(element);
        } catch (Exception e) {
            System.err.println(e.getMessage() + " in UnusedLocalVarChecker");
        }
    }

    private void checkAttributes(final Element element) {
        for (String tagInfo : usedTagAttributes) {
            final String[] parts = (tagInfo + "//").split("/");
            if (element.getName() != null && parts[0].equalsIgnoreCase(element.getName())) {
                final String name = element.getAttributeValue(parts[1]);
                if (name != null && localVariables.containsKey(name.toLowerCase())) {
                    localVariables.put(name.toLowerCase(), new VarInfo(name, true));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(final String name, final Object value) {
        super.setParameter(name, value);
        if ("UsedTagAttributes".equals(name)) {
            usedTagAttributes = getParameter("UsedTagAttributes", List.class);
        }
    }
    
    public static class VarInfo {
        private Boolean used;
        private Integer lineNumber;
        private String name;

        public VarInfo(final String name, final Boolean used) {
            this.name = name;
            this.used = used;
        }
    }
}
