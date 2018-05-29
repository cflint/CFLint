package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Collection;

import com.cflint.CF;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;

public class CFScopes {

    private static final Collection<String> scopes = Arrays.asList(CF.URL, CF.FORM, CF.COOKIE, CF.CGI, CF.SERVER,
            CF.APPLICATION, CF.SESSION, CF.CLIENT, CF.REQUEST, CF.ARGUMENTS, CF.VARIABLES, CF.THIS, CF.LOCAL,
            CF.CFCATCH);

    protected String[] parts(final String variable) {
        return variable.toLowerCase().split("\\.|\\[|\\]");
    }

    public boolean isCFScoped(final String variable) {
        final String[] parts = parts(variable);
        return scopes.contains(parts[0].toLowerCase());
    }

    public String getScope(final String variable) {
        final String[] parts = parts(variable);
        if (scopes.contains(parts[0].toLowerCase())) {
            return parts[0].toLowerCase();
        }
        return "variables";
    }

    public String getScope(final CFFullVarExpression variable) {
        final CFExpression part1 = variable.decomposeExpression().get(0);
        if (scopes.contains(part1.Decompile(0).toLowerCase())) {
            return part1.Decompile(0).toLowerCase();
        }
        return "variables";
    }

    public boolean isScoped(final String variable, final String scope) {
        final String[] parts = parts(variable);
        return parts[0].equalsIgnoreCase(scope);
    }

    public boolean isScoped(final CFFullVarExpression variable) {
        final CFExpression part1 = variable.decomposeExpression().get(0);
        return scopes.contains(part1.Decompile(0).toLowerCase());
    }

    public boolean isScoped(final CFFullVarExpression variable, final String scope) {
        final CFExpression part1 = variable.decomposeExpression().get(0);
        return scope != null && scope.equalsIgnoreCase(part1.Decompile(0).toLowerCase());
    }

    public boolean isLocalScoped(final String variable) {
        return isScoped(variable, CF.LOCAL);
    }

    public boolean isVariablesScoped(final String variable) {
        return isScoped(variable, CF.VARIABLES);
    }

    public boolean isFunctionScoped(final String variable) {
        return isScoped(variable, CF.LOCAL) || isScoped(variable, CF.VARIABLES) || isScoped(variable, CF.ARGUMENTS)
                || isScoped(variable, CF.CFCATCH);
    }

}
