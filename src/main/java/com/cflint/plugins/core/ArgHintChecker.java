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

public class ArgHintChecker extends CFLintScannerAdapter {

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

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement funcDeclStatement = (CFFuncDeclStatement) expression;
            final String _mlText = PrecedingCommentReader.getMultiLine(context, expression.getToken());
            final String mlText = _mlText == null ? null
                    : _mlText.replaceFirst("^/\\*", "").replaceAll("\\*/$", "").trim();

            // Read the function comments to get the javadoc style annotations
            final Map<String, String> annotations = new HashMap<String, String>();
            if (mlText != null && !mlText.isEmpty()) {
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

            for (final CFFunctionParameter expr : funcDeclStatement.getFormals()) {
                if (expr != null && expr.getName() != null && !annotations.containsKey(expr.getName().toLowerCase())) {
                    context.addMessage("ARG_HINT_MISSING_SCRIPT", expr.getName());
                }
            }

        }
    }
}
