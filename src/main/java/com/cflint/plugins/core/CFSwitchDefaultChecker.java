package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.CFSwitchStatement;
import net.htmlparser.jericho.Element;

public class CFSwitchDefaultChecker extends CFLintScannerAdapter {
    final String CFML_TAG_CHECK = "cfswitch";
    final String CFML_TAG_REQUIRED = "cfdefaultcase";
    final String CFSCRIPT_STATEMENT_REQUIRED = "default";
    final String SEVERITY = "WARNING";
    final String MESSAGE_CODE = "NO_DEFAULT_INSIDE_SWITCH";
    final String MESSAGE = "Not having a Default statement defined for a switch could pose potential issues";

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        // see if expression is cfSwithStatement
        if (expression instanceof CFSwitchStatement) {
            final CFSwitchStatement switchStatement = (CFSwitchStatement) expression;
            final String statement = switchStatement.Decompile(0).replace(" ", "").toLowerCase();
            if (!statement.contains(CFSCRIPT_STATEMENT_REQUIRED)) {
                context.addMessage(MESSAGE_CODE, null);
            }
        }
        // if cfSwitchStatement, then continue to look for cfDefaultStatement
        // if no cfDefaultStatement found, throw a bug.
    }

    // rule is: provide a default for switch statements to fall through
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String tagName = element.getName();
        if (tagName.equalsIgnoreCase(CFML_TAG_CHECK)) {
            boolean isDefault = false;
            for (final Element el : element.getChildElements()) {
                // decide if default was provided
                if (el.getName().equalsIgnoreCase(CFML_TAG_REQUIRED)) {
                    // default found, so reassign and break
                    isDefault = true;
                    break;
                }
            }
            if (!isDefault) { // no default found
                context.addMessage(MESSAGE_CODE, null);
            }
        }
    }
}