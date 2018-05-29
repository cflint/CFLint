package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

/**
 * Check for missing function hint attributes.
 */
public class FunctionHintChecker extends HintChecker {
    private static final String FUNCTION_HINT_MISSING = "FUNCTION_HINT_MISSING";

    /**
     * Parse a CF function tag declaration to see if it's missing a hint.
     */
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFFUNCTION)) {
            final String hint = element.getAttributeValue("hint");
            if (hint == null || hint.trim().isEmpty()) {
                context.addMessage(FUNCTION_HINT_MISSING, context.getFunctionName());
            }
        }
    }

    /**
     * Parse a CF function deceleration to see if it's missing a hint.
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement funcDeclStatement = (CFFuncDeclStatement) expression;
            final CFExpression hintAttribute = CFTool.convertMap(funcDeclStatement.getAttributes()).get("hint");
            if (hintAttribute == null) {
                checkHint(FUNCTION_HINT_MISSING, context.getFunctionName(), expression, context);
            }
        }
    }
}
