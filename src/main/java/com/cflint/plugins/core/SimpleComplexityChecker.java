package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFCase;
import cfml.parsing.cfscript.script.CFDoWhileStatement;
import cfml.parsing.cfscript.script.CFForInStatement;
import cfml.parsing.cfscript.script.CFForStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFIfStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.CFSwitchStatement;
import cfml.parsing.cfscript.script.CFTryCatchStatement;
import cfml.parsing.cfscript.script.CFWhileStatement;
import net.htmlparser.jericho.Element;

public class SimpleComplexityChecker extends CFLintScannerAdapter {
    protected static final int COMPLEXITY_THRESHOLD = 10;
    protected int complexity = 0;
    protected boolean alreadyTooComplex = false;
    private int functionLineNo = 1;
    private int functionOffset = 0;

    @Override
    public void startFile(final String fileName, final BugList bugs) {
        complexity = 0;
        alreadyTooComplex = false;
        functionLineNo = 1;
    }

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        CFFuncDeclStatement function;

        if (expression != null) {
            if (expression instanceof CFFuncDeclStatement) {
                function = (CFFuncDeclStatement) expression;
                functionLineNo = function.getLine();
                functionOffset = function.getOffset();
                complexity = 0;
                alreadyTooComplex = false;
            }
            // Not using instanceof to avoid double counting
            else if (expression.getClass().equals(CFIfStatement.class) || expression.getClass().equals(CFForStatement.class)
                || expression.getClass().equals(CFForInStatement.class)
                || expression.getClass().equals(CFSwitchStatement.class)
                || expression.getClass().equals(CFTryCatchStatement.class)
                || expression.getClass().equals(CFWhileStatement.class)
                || expression.getClass().equals(CFCase.class)
                || expression.getClass().equals(CFDoWhileStatement.class)) {
                complexity++;
                checkComplexity(context.getFunctionName(), functionLineNo, functionOffset, context, bugs);
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String name = element.getName();

        if (name.equalsIgnoreCase(CF.CFFUNCTION)) {
            functionLineNo = element.getSource().getRow(element.getBegin());
            functionOffset = element.getBegin();
            complexity = 0;
            alreadyTooComplex = false;
        } else {
            if (name.equalsIgnoreCase(CF.CFIF) || name.equalsIgnoreCase(CF.CFELSE) || name.equalsIgnoreCase(CF.CFELSEIF)
                || name.equalsIgnoreCase(CF.CFLOOP) || name.equalsIgnoreCase(CF.CFWHILE)
                || name.equalsIgnoreCase(CF.CFOUTPUT) // TODO could check for
                // query=
                || name.equalsIgnoreCase(CF.CFCASE) || name.equalsIgnoreCase(CF.CFDEFAULTCASE)
                || name.equalsIgnoreCase(CF.CFTRY) || name.equalsIgnoreCase(CF.CFCATCH)) {
                complexity++;
                checkComplexity(context.getFunctionName(), functionLineNo, functionOffset, context, bugs);
            }
        }
    }

    protected void checkComplexity(final String name, final int lineNo, final int offset, final Context context, final BugList bugs) {
        final String complexityThreshold = context.getConfiguration().getParameter(this,"maximum");
        int threshold = COMPLEXITY_THRESHOLD;

        if (complexityThreshold != null) {
            threshold = Integer.parseInt(complexityThreshold);
        }

        if (!alreadyTooComplex && complexity > threshold) {
            alreadyTooComplex = true;

            context.addMessage("FUNCTION_TOO_COMPLEX", null, this, lineNo, offset);
        }
    }

}
