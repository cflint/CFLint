package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Collection;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import com.cflint.CF;

public class CFScopes {

    private static final Collection<String> scopes = Arrays.asList(CF.URL, CF.FORM, CF.COOKIE, CF.CGI, CF.SERVER, CF.APPLICATION,
        CF.SESSION, CF.CLIENT, CF.REQUEST, CF.ARGUMENTS, CF.VARIABLES, CF.THIS, CF.LOCAL, CF.CFCATCH, CF.CFTHREAD);

    protected static String[] parts(final String variable) {
        return variable.toLowerCase().split("\\.|\\[|\\]");
    }
    protected static String[] partsCase(final String variable) {
        return variable.split("\\.|\\[|\\]");
    }

    public static boolean isCFScoped(final String variable) {
        final String[] parts = parts(variable);
        return scopes.contains(parts[0].toLowerCase());
    }
    public static String descope(final String variable) {
        final String[] parts = partsCase(variable);
        return parts[parts.length-1];
    }
    public static String getScope(final String variable) {
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


    public static boolean isScope(final String scope) {
        return scope !=null && scopes.contains(scope.toLowerCase());
    }
    public static boolean isScoped(final String variable, final String scope) {
        final String[] parts = parts(variable);
        return parts[0].equalsIgnoreCase(scope);
    }
    public static boolean isScoped(final CFFullVarExpression variable) {
        CFExpression part1 = variable.decomposeExpression().get(0);
        return scopes.contains(part1.Decompile(0).toLowerCase());
    }
    public boolean isScoped(final CFFullVarExpression variable, final String scope) {
        CFExpression part1 = variable.decomposeExpression().get(0);
        return scope != null && scope.equalsIgnoreCase(part1.Decompile(0).toLowerCase());
    }

    public boolean isLocalScoped(final String variable) {
        return isScoped(variable, CF.LOCAL);
    }
    public boolean isVariablesScoped(final String variable) {
        return isScoped(variable, CF.VARIABLES);
    }

    public boolean isFunctionScoped(final String variable) {
        return isScoped(variable, CF.LOCAL) || isScoped(variable, CF.VARIABLES) || isScoped(variable, CF.ARGUMENTS) || isScoped(variable, CF.CFCATCH);
    }

}
