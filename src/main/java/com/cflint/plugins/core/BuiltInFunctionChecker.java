package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.script.CFAbortStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import ro.fortsoft.pf4j.Extension;
/**
 * isDate() is too permissive. Avoid it.
 * @author Ryan
 *
 */
@Extension
public class BuiltInFunctionChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if(expression instanceof CFFunctionExpression){
            final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
            if(functionExpression.getName().equalsIgnoreCase("isDate")){
                context.addMessage("AVOID_USING_ISDATE", null);
                
            }
        }
    }
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
    }
}