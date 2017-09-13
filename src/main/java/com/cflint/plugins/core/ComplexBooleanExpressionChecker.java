package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.CFExpression;

public class ComplexBooleanExpressionChecker extends CFLintScannerAdapter {
     protected int complexThreshold = 10;

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFBinaryExpression) {
            final String code = expression.Decompile(0).toLowerCase();

            if (isComplex(code, complexThreshold)) {
                final int lineNo = currentLine(expression, context);

                complexBooleanExpression(expression.getOffset(), lineNo, context, bugs);
            }
        }
    }

    protected boolean isComplex(final String code, final int complexThreshold) {
        final int noAnds = noSubstrings(code, " && ") + noSubstrings(code, " and ");
        final int noOrs = noSubstrings(code, " || ") + noSubstrings(code, " or ");
        final int noNots = noSubstrings(code, " ! ") + noSubstrings(code, " not ");

        // This is just a rough heuristic
        // lots of and's or or's on their own is usually easy to understand
        // but combine them together and it gets hard to understand very quickly
        return (noAnds * noOrs + noNots + noAnds + noOrs) > complexThreshold;
    }

    protected int noSubstrings(final String string, final String substring) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {
            lastIndex = string.indexOf(substring, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += substring.length();
            }
        }

        return count;
    }

    public void complexBooleanExpression(final int lineNo, final int offset, final Context context, final BugList bugs) {
        context.addMessage("COMPLEX_BOOLEAN_CHECK", null, this, lineNo, offset);
    }

}
