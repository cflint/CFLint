package com.cflint.plugins.core;

import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.PrecedingCommentReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cfml.parsing.cfscript.script.CFScriptStatement;

/**
 * Check for missing hint attributes.
 */
public class HintChecker extends CFLintScannerAdapter {

    /**
     * Check for missing hint attributes.
     *
     * @param message message to report.
     * @param expression expression to scan.
     * @param context expression context.
     */
    protected void checkHint(final String message, final String name, final CFScriptStatement expression, final Context context) {
        final String multiLineText = PrecedingCommentReader.getMultiLine(context, expression.getToken());
        final String mlText = multiLineText == null ? null
                : multiLineText.replaceFirst("^/\\*", "").replaceAll("\\*/$", "").trim();
        if (mlText != null && !mlText.isEmpty()) {
            final Pattern pattern = Pattern.compile(".*\\s*@hint\\s+([\\w,_]+)\\s*.*", Pattern.DOTALL);
            final Matcher matcher = pattern.matcher(mlText);
            if (matcher.matches()) {
                String hintText = matcher.group(1);
                if (hintText.trim().isEmpty()) {
                    context.addMessage(message, name);
                }
            }
        } else {
            context.addMessage(message, name);
        }
    }
}
