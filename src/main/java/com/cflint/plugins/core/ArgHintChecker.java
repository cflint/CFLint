package com.cflint.plugins.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.PrecedingCommentReader;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

/**
 * Check if a functions argument is missing a hint.
 */
public class ArgHintChecker extends CFLintScannerAdapter {

    /**
     * Parse a CF argument tag to see if the argument hint is missing.
     */
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFARGUMENT)) {
            final String name = element.getAttributeValue(CF.NAME);
            final String hint = element.getAttributeValue(CF.HINT);
            if (hint == null || hint.length() == 0) {
                context.addMessage("ARG_HINT_MISSING", name);
            }
        }
    }

    /**
     * Parse a CFScript function declaration to see if any of the arguments hints are missing.
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement funcDeclStatement = (CFFuncDeclStatement) expression;
            final String multiLineText = PrecedingCommentReader.getMultiLine(context, expression.getToken());
            final String mlText = multiLineText == null ? null
                    : multiLineText.replaceFirst("^/\\*", "").replaceAll("\\*/$", "").trim();
            
            final Map<String, String> annotations = new HashMap<>();
            if (mlText != null && !mlText.isEmpty()) {
                readCommentAnnotations(mlText, annotations);
            }

            for (final CFFunctionParameter expr : funcDeclStatement.getFormals()) {
                if (expr != null && expr.getName() != null && !annotations.containsKey(expr.getName().toLowerCase())) {
                    context.addMessage("ARG_HINT_MISSING_SCRIPT", expr.getName());
                }
            }
        }
    }

    /**
     * Parse a function comment to see if there are any argument annotations.
     *
     * @param mlText multi line text to search for argument hints in comments.
     * @param annotations function argumnet hints.
     */
    private void readCommentAnnotations(final String mlText, final Map<String, String> annotations) {
        final Pattern pattern = Pattern.compile("^.*\\s*@(\\w+)\\s+(.*+)$");
        BufferedReader reader = new BufferedReader(new StringReader(mlText));
        try {
            String line = reader.readLine();
            while (line != null) {
                final Matcher matcher = pattern.matcher(line.trim());
                if (matcher.matches()) {
                    annotations.put(matcher.group(1).trim().toLowerCase(), matcher.group(2).trim());
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
