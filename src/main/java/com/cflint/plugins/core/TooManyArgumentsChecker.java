package com.cflint.plugins.core;

import com.cflint.Levels;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class TooManyArgumentsChecker extends CFLintScannerAdapter {
    final int ARGUMENT_THRESHOLD = 10;
    final Levels severity = Levels.WARNING;

    protected int argumentCount = 0;
    protected int functionLine = 0;

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            final int begLine = function.getLine();
            final int noArguments = function.getFormals().size();

            checkNumberArguments(noArguments, begLine, context, bugs);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals("cffunction")) {
            functionLine = element.getSource().getRow(element.getBegin());
            argumentCount = 0;
        } else if (element.getName().equals("cfargument")) {
            argumentCount++;
        }
        // No easy way of detecting end tag so assumes functions will contain
        // some code
        // otherwise the argument count will be off by one
        else if (!element.getName().equals("!---") && argumentCount > 0) {
            checkNumberArguments(argumentCount, functionLine, context, bugs);
            argumentCount = 0;
            functionLine = 0;
        }
    }

    protected void checkNumberArguments(final int argumentCount, final int atLine, final Context context,
            final BugList bugs) {
        final String argumentThreshold = getParameter("maximum");
        int threshold = ARGUMENT_THRESHOLD;

        if (argumentThreshold != null) {
            threshold = Integer.parseInt(argumentThreshold);
        }

        if (argumentCount > threshold) {
            context.addUniqueMessage("EXCESSIVE_ARGUMENTS", context.getFunctionName(), this, atLine);
        }
    }

}
