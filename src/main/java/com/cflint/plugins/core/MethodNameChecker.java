package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class MethodNameChecker extends CFLintScannerAdapter {
    public static final String METHOD_NAME = "Method name ";
    final String severity = "INFO";

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement method = (CFFuncDeclStatement) expression;
            final int lineNo = method.getLine() + context.startLine() - 1;
            checkNameForBugs(context, lineNo);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals("cffunction")) {
            final int lineNo = element.getSource().getRow(element.getBegin());
            checkNameForBugs(context, lineNo);
        }
    }

    public void checkNameForBugs(final Context context, final int line) {
        final String method = context.getFunctionName();
        int minMethodLength = ValidName.MIN_METHOD_LENGTH;
        int maxMethodLength = ValidName.MAX_METHOD_LENGTH;
        int maxMethodWords = ValidName.MAX_METHOD_WORDS;

        if (getParameter("MinLength") != null) {
            try {
                minMethodLength = Integer.parseInt(getParameter("MinLength"));
            } catch (final Exception e) {
            }
        }

        if (getParameter("MaxLength") != null) {
            try {
                maxMethodLength = Integer.parseInt(getParameter("MaxLength"));
            } catch (final Exception e) {
            }
        }

        if (getParameter("MaxWords") != null) {
            try {
                maxMethodWords = Integer.parseInt(getParameter("MaxWords"));
            } catch (final Exception e) {
            }
        }

        final ValidName name = new ValidName(minMethodLength, maxMethodLength, maxMethodWords);

        if (name.isInvalid(method)) {
            context.addMessage("METHOD_INVALID_NAME", null, line);
        }
        if (name.isUpperCase(method)) {
            context.addMessage("METHOD_ALLCAPS_NAME", null, line);
        }
        if (name.tooShort(method)) {
            context.addMessage("METHOD_TOO_SHORT", null, line);
        }
        if (name.tooLong(method)) {
            context.addMessage("METHOD_TOO_LONG", null, line);
        }
        if (!name.isUpperCase(method) && name.tooWordy(method)) {
            context.addMessage("METHOD_TOO_WORDY", null, line);
        }
        if (name.isTemporary(method)) {
            context.addMessage("METHOD_IS_TEMPORARY", null, line);
        }
        if (name.hasPrefixOrPostfix(method)) {
            context.addMessage("METHOD_HAS_PREFIX_OR_POSTFIX", null, line);
        }
    }
}