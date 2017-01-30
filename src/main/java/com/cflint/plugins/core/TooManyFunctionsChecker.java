package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.plugins.Context.ContextType;

import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class TooManyFunctionsChecker extends CFLintScannerAdapter {
    final String severity = "WARNING";
    final int FUNCTION_THRESHOLD = 10;

    protected int functionCount = 0;

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFCompDeclStatement) {
            functionCount = 0;
        } else if (expression instanceof CFFuncDeclStatement) {

            if (!trivalFunction(context.getFunctionName())) {
                functionCount++;
                checkNumberFunctions(functionCount, 1, context, bugs);
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals("cfcomponent")) {
            functionCount = 0;
        } else if (element.getName().equals("cffunction") && !trivalFunction(context.getFunctionName())) {
            functionCount++;
            checkNumberFunctions(functionCount, 1, context, bugs);
        }
    }

    protected boolean trivalFunction(final String name) {
        final int length = name.length();
        return length >= 3 && name.substring(1, 3).equalsIgnoreCase("get")
                || length >= 3 && name.substring(1, 3).equalsIgnoreCase("set")
                || length >= 2 && name.substring(1, 2).equalsIgnoreCase("is");
    }

    protected void checkNumberFunctions(final int functionCount, final int atLine, final Context context,
            final BugList bugs) {
        final String functionThreshold = getParameter("maximum");
        int threshold = FUNCTION_THRESHOLD;

        if (functionThreshold != null) {
            threshold = Integer.parseInt(functionThreshold);
        }

        if (functionCount > threshold) {
            context.getParent(ContextType.Component).addUniqueMessage("EXCESSIVE_FUNCTIONS", context.getFilename(),
                    this, atLine);
        }
    }

}
