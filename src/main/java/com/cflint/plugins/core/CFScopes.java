package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Collection;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;

public class CFScopes {

    public static final String LOCAL = "local";
    final static Collection<String> scopes = Arrays.asList("url", "form", "cookie", "cgi", "server", "application",
            "session", "client", "request", "arguments", "variables", "this", LOCAL, "cfcatch");

    protected String[] parts(final String variable) {
        return variable.toLowerCase().split("\\.|\\[|\\]");
    }

    public boolean isCFScoped(final String variable) {
        final String[] parts = parts(variable);
        return scopes.contains(parts[0].toLowerCase());
    }
    public String getScope(final String variable) {
        final String[] parts = parts(variable);
        if(scopes.contains(parts[0].toLowerCase())){
            return parts[0].toLowerCase();
        }
        return "variables";
    }
    public String getScope(final CFFullVarExpression variable) {
        CFExpression part1 = variable.decomposeExpression().get(0);
        if(scopes.contains(part1.Decompile(0).toLowerCase())){
            return part1.Decompile(0).toLowerCase();
        }
        return "variables";
    }


    public boolean isScoped(final String variable, final String scope) {
        final String[] parts = parts(variable);
        return parts[0].equalsIgnoreCase(scope);
    }
    public boolean isScoped(final CFFullVarExpression variable) {
        CFExpression part1 = variable.decomposeExpression().get(0);
        return scopes.contains(part1.Decompile(0).toLowerCase());
    }
    public boolean isScoped(final CFFullVarExpression variable,String scope) {
        CFExpression part1 = variable.decomposeExpression().get(0);
        return scope != null && scope.equalsIgnoreCase(part1.Decompile(0).toLowerCase());
    }

    public boolean isLocalScoped(final String variable) {
        return isScoped(variable, LOCAL);
    }

    public boolean isFunctionScoped(final String variable) {
        return isScoped(variable, LOCAL) || isScoped(variable, "variables") || isScoped(variable, "arguments") || isScoped(variable, "cfcatch");
    }

}