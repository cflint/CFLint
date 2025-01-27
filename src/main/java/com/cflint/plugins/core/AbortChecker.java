package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFAbortStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import ro.fortsoft.pf4j.Extension;

@Extension
public class AbortChecker extends CFLintScannerAdapter {
    
    /** 
     * @param expression expression
     * @param context context
     * @param bugs bugs
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFAbortStatement) {
            context.addMessage("AVOID_USING_ABORT", null);
        }
    }
}
