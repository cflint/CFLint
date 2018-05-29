package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class TooManyArgumentsChecker extends CFLintScannerAdapter {
    private static final int ARGUMENT_THRESHOLD = 10;

    protected int argumentCount = 0;

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            final int noArguments = function.getFormals().size();

            checkNumberArguments(noArguments, context, bugs, function);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFFUNCTION)) {
            argumentCount = 0;
        } else if (element.getName().equals(CF.CFARGUMENT)) {
            argumentCount++;
        }
        // No easy way of detecting end tag so assumes functions will contain
        // some code
        // otherwise the argument count will be off by one
        else if (!element.getName().equals(CF.COMMENT) && argumentCount > 0) {
            checkNumberArguments(argumentCount, context, bugs,null);
            argumentCount = 0;
        }
    }

    protected void checkNumberArguments(final int argumentCount, final Context context,
            final BugList bugs, CFFuncDeclStatement expression) {
        final String argumentThreshold = context.getConfiguration().getParameter(this,"maximum");
        int threshold = ARGUMENT_THRESHOLD;

        if (argumentThreshold != null) {
            threshold = Integer.parseInt(argumentThreshold);
        }

        if (argumentCount > threshold) {
            context.addUniqueMessage(null,"EXCESSIVE_ARGUMENTS", context.getFunctionName(), this, expression==null?null:expression.getName());
        }
    }

}
