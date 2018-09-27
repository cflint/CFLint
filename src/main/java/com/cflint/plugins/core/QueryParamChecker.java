package com.cflint.plugins.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cflint.BugList;
import com.cflint.CF;
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
            if ("setSql".equalsIgnoreCase(functionExpression.getFunctionName()) || "queryExecute".equalsIgnoreCase(functionExpression.getFunctionName())
                && !functionExpression.getArgs().isEmpty()) {
                final CFExpression argsExpression = functionExpression.getArgs().get(0);
                final Pattern p = Pattern.compile(".*#(?:##)?([^#]+)(?:##)?#($|[^#]).*", Pattern.DOTALL);
                if (p.matcher(argsExpression.Decompile(0)).matches()) {
                    context.addMessage("QUERYPARAM_REQ", functionExpression.getName());
                }
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (
            element.getName().equalsIgnoreCase(CF.CFQUERY) && !CF.QUERY.equalsIgnoreCase(element.getAttributeValue(CF.DBTYPE))) {
            String content = element.getContent().toString();
            //Todo : cfparser/Jericho does not support parsing out the cfqueryparam very well.
            //   the following code will not work when there is a > sign in the expression
            content = content.replaceAll("<[cC][fF][qQ][uU][eE][rR][yY][pP][aA][rR][aA][mM][^>]*>", "");
            if (content.indexOf('#') >= 0) {
                final List<Integer> ignoreLines = determineIgnoreLines(element);
                final Matcher matcher = Pattern.compile("#(?:##)?([^#]+)(?:##)?#($|[^#])",Pattern.DOTALL).matcher(content);
                while (matcher.find()) {
                    if (matcher.groupCount() >= 1) {
                        int currentline = context.startLine() + countNewLinesUpTo(content, matcher.start());
                        int currentOffset = element.getStartTag().getEnd() + 1 + matcher.start();
                        final String variableName = matcher.group(1);
                        if (!ignoreLines.contains(currentline)) {
                            context.addMessage("CFQUERYPARAM_REQ", variableName, currentline, currentOffset);
                        }
                    }
                }
            }
        }
    }

    /**
     * Determine the line numbers of the <!--- @CFLintIgnore CFQUERYPARAM_REQ ---> tags
     * Both the current and the next line are included.
     *
     * @param element   the element object
     * @return          the line numbers of any @@CFLintIgnore annotations.
     */
    private List<Integer> determineIgnoreLines(final Element element) {
        final List<Integer> ignoreLines = new ArrayList<>();
        for (Element comment : element.getChildElements()) {
            if ("!---".equals(comment.getName()) && comment.toString().contains("@CFLintIgnore") && comment.toString().contains("CFQUERYPARAM_REQ")) {
                int ignoreLine = comment.getSource().getRow(comment.getEnd());
                ignoreLines.add(ignoreLine);
                ignoreLines.add(ignoreLine + 1);
                ignoreLines.add(comment.getSource().getRow(comment.getBegin()));
            } else {
                ignoreLines.addAll(determineIgnoreLines(comment));
            }
        }
        return ignoreLines;
    }

    /**
     * Count the number of new lines
     * @param val  the string to count lines
     * @param pos  the position to start
     * @return          the number of new lines
     */
    public int countNewLinesUpTo(final String val, final int pos) {
        final String x = pos > val.length() ? val : val.substring(0, pos);
        return Math.max(0, x.split("\\R").length - 1);
    }

}
