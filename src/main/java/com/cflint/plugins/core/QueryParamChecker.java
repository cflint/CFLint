package com.cflint.plugins.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import net.htmlparser.jericho.Element;

public class QueryParamChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFunctionExpression) {
            final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
            if (functionExpression.getFunctionName().equalsIgnoreCase("setSql")
                    && !functionExpression.getArgs().isEmpty()) {
                final CFExpression argsExpression = functionExpression.getArgs().get(0);
                final Pattern p = Pattern.compile(".*#[^#].*", Pattern.DOTALL);
                if (p.matcher(argsExpression.Decompile(0)).matches()) {
                    context.addMessage("QUERYPARAM_REQ", functionExpression.getName());
                }
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (
        element.getName().equals("cfquery") && !"query".equalsIgnoreCase(element.getAttributeValue("dbtype"))) {
            String content = element.getTextExtractor().toString();
            //Todo : Jericho does not support parsing out the cfqueryparam very well.
            //   the following code will not work when there is a > sign in the expression
            content = content.replaceAll("<cfqueryparam[^>]*>", "");
            if (content.indexOf("#") > 0) {
                final Pattern pattern = Pattern.compile("#(.+?)#");
                final Matcher matcher = pattern.matcher(content);
                while (matcher.find()) {
                    if (matcher.groupCount() >= 1) {
                        // final String variableName = matcher.group(1);
                        context.addMessage("CFQUERYPARAM_REQ", element.getAttributeValue("NAME"));
                    }
                }
            }
        }
    }

}
