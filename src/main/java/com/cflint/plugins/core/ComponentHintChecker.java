package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

/**
 * Check for missing component hint attributes.
 */
public class ComponentHintChecker extends HintChecker {
    private static final String COMPONENT_HINT_MISSING = "COMPONENT_HINT_MISSING";

    /**
     * Parse a CF component tag declaration to see if it's missing a hint.
     */
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFCOMPONENT)) {
            final String hint = element.getAttributeValue(CF.HINT);
            if (hint == null || hint.trim().isEmpty()) {
                context.addMessage(COMPONENT_HINT_MISSING, context.calcComponentName());
            }
        }
    }

    /**
     * Parse a CF component deceleration to see if it's missing a hint.
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFCompDeclStatement) {
            final CFCompDeclStatement compDeclStatement = (CFCompDeclStatement) expression;
            final CFExpression hintAttribute = CFTool.convertMap(compDeclStatement.getAttributes()).get("hint");
            if (hintAttribute == null) {
                checkHint(COMPONENT_HINT_MISSING, context.calcComponentName(), expression, context);
            }
        }
    }

}
