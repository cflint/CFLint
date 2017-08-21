package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.plugins.Context.ContextType;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class TooManyFunctionsChecker extends CFLintScannerAdapter {
    private static final int FUNCTION_THRESHOLD = 10;

    protected int functionCount = 0;

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {

            if (!trivalFunction(context.getFunctionName())) {
                functionCount++;
                checkNumberFunctions(functionCount, 1, context, bugs);
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFFUNCTION) && !trivalFunction(context.getFunctionName())) {
            functionCount++;
            checkNumberFunctions(functionCount, 1, context, bugs);
        }
    }

    protected boolean trivalFunction(final String name) {
        final int length = name.length();
        return length >= 3 && "get".equalsIgnoreCase(name.substring(1, 3))
            || length >= 3 && "set".equalsIgnoreCase(name.substring(1, 3))
            || length >= 2 && "is".equalsIgnoreCase(name.substring(1, 2));
    }

    protected void checkNumberFunctions(final int functionCount, final int atLine, final Context context,
                                        final BugList bugs) {
        final String functionThreshold = getParameter("maximum");
        int threshold = FUNCTION_THRESHOLD;

        if (functionThreshold != null) {
            threshold = Integer.parseInt(functionThreshold);
        }

        if (functionCount == threshold + 1) {
            context.getParent(ContextType.COMPONENT).addUniqueMessage("EXCESSIVE_FUNCTIONS", null,
                this, atLine);
        }
    }

    @Override
    public void startComponent(final Context context, final BugList bugs) {
        functionCount = 0;
    }

}
