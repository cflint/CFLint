package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;

public class GlobalVarChecker extends CFLintScannerAdapter {
    protected CFScopes scopes = new CFScopes();

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if ((context.isInComponent() || context.isInFunction()) && expression instanceof CFFullVarExpression) {
            final CFExpression firstExpression = ((CFFullVarExpression) expression).getExpressions().get(0);
            if (firstExpression instanceof CFIdentifier) {
                doIdentifier((CFIdentifier) firstExpression, context, bugs);
            }
        }
    }

    protected void doIdentifier(final CFIdentifier expression, final Context context, final BugList bugs) {
        final String name = expression.getName();
        if (scopes.isCFScoped(name) && !scopes.isFunctionScoped(name) && !context.getCallStack().isVariable(name)
                && !context.getCallStack().isArgument(name)
                && context.getCallStack().getPluginVar(GlobalVarChecker.class, name) == null) {
            context.getCallStack().setPluginVar(GlobalVarChecker.class, name, true);
            context.addMessage("GLOBAL_VAR", name);
        }

    }

}
