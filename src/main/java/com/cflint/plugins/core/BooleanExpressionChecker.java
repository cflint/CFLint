package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.CFExpression;

public class BooleanExpressionChecker extends CFLintScannerAdapter {
    protected int lastLineNo = -1;

    // Reset the last line number
    @Override
    public void startFile(final String fileName, final BugList bugs) {
        super.startFile(fileName, bugs);
        lastLineNo = -1;
    }

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFBinaryExpression) {
            final String code = expression.Decompile(0).toLowerCase();

            if (hasExplicitBooleanCheck(code)) {
                final int lineNo = currentLine(expression, context);

                // Only report issue once per line
                if (lastLineNo != lineNo) {
                    context.addMessage("EXPLICIT_BOOLEAN_CHECK", null);
                    lastLineNo = lineNo;
                }
            }
        }

    }

    protected boolean hasExplicitBooleanCheck(final String code) {
        return code.contains("== true") || code.contains("eq true") || code.contains("is true")
                || code.contains("!= true") || code.contains("== false") || code.contains("eq false")
                || code.contains("is false") || code.contains("!= false");
    }

}
