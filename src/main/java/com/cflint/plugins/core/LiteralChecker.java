package com.cflint.plugins.core;

import java.util.HashMap;
import java.util.Map;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFLiteral;
import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import ro.fortsoft.pf4j.Extension;

@Extension
public class LiteralChecker extends CFLintScannerAdapter {
    protected static final int REPEAT_THRESHOLD = 3;
    protected static final int WARNING_THRESHOLD = 5;

    protected int threshold = REPEAT_THRESHOLD;
    protected int warningThreshold = WARNING_THRESHOLD;

    protected Map<String, Integer> globalLiterals = new HashMap<>();
    protected Map<String, Integer> functionListerals = new HashMap<>();

    // May want to consider resetting literal map on new components but this way
    // it detects duplicated literals across files which is useful

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        final String repeatThreshold = getParameter("Maximum");
        final String maxWarnings = getParameter("MaxWarnings");
        final String warningScope = getParameter("WarningScope");

        if (repeatThreshold != null) {
            threshold = Integer.parseInt(repeatThreshold);
        }

        if (maxWarnings != null) {
            warningThreshold = Integer.parseInt(maxWarnings);
        }

        if (expression instanceof CFLiteral) {
            final CFLiteral literal = (CFLiteral) expression;
            final String name = literal.Decompile(0).replace("'", "");

            if (isCommon(name)) {
                return;
            }

            final int lineNo = literal.getLine() + context.startLine() - 1;
            final int offset = literal.getOffset() + context.offset();

            if ((warningScope == null || "global".equals(warningScope)) && !context.isInFunction()) {
                literalCount(name, lineNo, offset, globalLiterals, true, context, bugs);
            } else if ("local".equals(warningScope) && context.isInFunction()) {
                literalCount(name, lineNo, offset, functionListerals, false, context, bugs);
            }
        }
    }

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFCompDeclStatement) {
            functionListerals.clear();
        }
    }

    protected void literalCount(final String name, final int lineNo, final int offset, final Map<String, Integer> literals,
                                final boolean global, final Context context, final BugList bugs) {
        int count = 1;

        if (literals.get(name) == null) {
            literals.put(name, count);
        } else {
            count = literals.get(name);
            count++;
            literals.put(name, count);
        }

        if (count > threshold && (warningThreshold == -1 || (count - threshold) <= warningThreshold)) {
            if (global) {
                magicGlobalValue(name, lineNo, offset, context, bugs);
            } else {
                magicLocalValue(name, lineNo, offset, context, bugs);
            }
        }
    }

    protected boolean isCommon(final String name) {
        return "1".equals(name) || "0".equals(name) || "".equals(name) || "true".equals(name) || "false".equals(name);
    }

    /**
     * Checks if the literal is a special case that should not fire the literal checker rule
     *
     * @param name
     * @return
     */
    private boolean isSpecial(final String name) {
        //Empty literals do not flag
        if (name == null || name.length() == 0) {
            return true;
        }
        //Punctuation literals excepted (Look for absence of a word character)
        if (!name.matches(".*\\w.*")) {
            return true;
        }
        //Exclude datatype for cfquery/cfproc
        if (name.startsWith("cf_sql_")) {
            return true;
        }
        //Check ignore words list from the configuration
        return getParameterAsList("IgnoreWords").contains(name.toLowerCase());
    }

    public void magicLocalValue(final String name, final int lineNo, final int offset, final Context context, final BugList bugs) {
        if (!isSpecial(name.toLowerCase())) {
            context.addUniqueMessage("LOCAL_LITERAL_VALUE_USED_TOO_OFTEN", name, this, lineNo, offset);
        }
    }

    public void magicGlobalValue(final String name, final int lineNo, final int offset, final Context context, final BugList bugs) {
        if (!isSpecial(name.toLowerCase())) {
            context.addUniqueMessage("GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN", name, this, lineNo, offset);
        }
    }
}
