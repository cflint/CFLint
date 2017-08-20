package com.cflint.plugins.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cflint.CF;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;
import com.cflint.tools.PrecedingCommentReader;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class FunctionHintChecker extends CFLintScannerAdapter {
    private final String functionHintMissing = "FUNCTION_HINT_MISSING";

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFFUNCTION)) {
            final String hint = element.getAttributeValue("hint");
            if (hint == null || hint.trim().isEmpty()) {
                context.addMessage(functionHintMissing, context.getFunctionName());
            }
        }
    }

    @Override
    public void expression(final CFScriptStatement expression, final Context context, BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement funcDeclStatement = (CFFuncDeclStatement) expression;
            final CFExpression hintAttribute = CFTool.convertMap(funcDeclStatement.getAttributes()).get("hint");
            if (hintAttribute == null) {
                final String _mlText = PrecedingCommentReader.getMultiLine(context, expression.getToken());
                final String mlText = _mlText == null ? null
                        : _mlText.replaceFirst("^/\\*", "").replaceAll("\\*/$", "").trim();
                if (mlText != null && !mlText.isEmpty()) {
                    final Pattern pattern = Pattern.compile(".*\\s*@hint\\s+([\\w,_]+)\\s*.*", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(mlText);
                    if (matcher.matches()) {
                        String hintText = matcher.group(1);
                        if (hintText.trim().isEmpty()) {
                            context.addMessage(functionHintMissing, context.getFunctionName());
                        }
                    }
                } else {
                    context.addMessage(functionHintMissing, context.getFunctionName());
                }
            }
        }
    }
}
