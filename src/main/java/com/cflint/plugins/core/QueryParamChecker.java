package com.cflint.plugins.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.StringReader;
import java.io.BufferedReader;

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
            final String allowVariableExpression = context.getConfiguration().getParameter(this,"allowVariableExpression");
            Pattern allowVariableExpressionPattern = null;
            if ( !"".equals(allowVariableExpression) ) {
                allowVariableExpressionPattern = Pattern.compile(allowVariableExpression,Pattern.DOTALL);
            }
            final String allowLineExpression = context.getConfiguration().getParameter(this,"allowLineExpression");
            //Todo : cfparser/Jericho does not support parsing out the cfqueryparam very well.
            //   the following code will not work when there is a > sign in the expression
            content = content.replaceAll("<[cC][fF][qQ][uU][eE][rR][yY][pP][aA][rR][aA][mM][^>]*>", "");
            if (content.indexOf('#') >= 0) {
                final List<Integer> ignoreLines = determineIgnoreLines(content, context.startLine());
                final Matcher matcher = Pattern.compile("#(?:##)?([^#]+)(?:##)?#($|[^#])",Pattern.DOTALL).matcher(content);
                while (matcher.find()) {
                    if (matcher.groupCount() >= 1) {
                        int currentline = context.startLine() + countNewLinesUpTo(content, matcher.start());
                        String linecontent = content.split("\\R")[currentline-context.startLine()];
                        int currentOffset = element.getStartTag().getEnd() + 1 + matcher.start();
                        String variableName = matcher.group(1);
                        Pattern allowLineExpressionPattern = null;
                        if ( !"".equals(allowLineExpression) ) {
                            //System.out.println(allowLineExpression.replaceAll("\\$\\{variable\\}","\\\\Q" + Matcher.quoteReplacement(variableName) + "\\\\E"));
                            allowLineExpressionPattern = Pattern.compile(allowLineExpression.replaceAll("\\$\\{variable\\}","\\\\Q" + Matcher.quoteReplacement(variableName) + "\\\\E"),Pattern.DOTALL);
                        }
                        if ( !ignoreLines.contains(currentline) 
                            && (allowVariableExpressionPattern == null || !allowVariableExpressionPattern.matcher(variableName).find())
                            && (allowLineExpressionPattern == null || !allowLineExpressionPattern.matcher(linecontent).find()))  {
                            //System.out.println("linecontent:" + linecontent);
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
    private List<Integer> determineIgnoreLines(final String textContent, final int start ) {

        final List<Integer> ignoreLines = new ArrayList<>();
        int currentline = start;
        String line = null;
        List<Integer> tmpIgnoreLines = new ArrayList<>();
        int match = 0;

        BufferedReader bufReader = new BufferedReader(new StringReader(textContent));
        
        try {
            while( (line=bufReader.readLine()) != null )
            {
                if ( line.contains("!---") ) {
                    if (!tmpIgnoreLines.contains(currentline)) {
                        tmpIgnoreLines.add(currentline);
                    }
                    match = 1;
                }

                if ( line.contains("@CFLintIgnore") && match > 0 ) {
                    if (!tmpIgnoreLines.contains(currentline)) {
                        tmpIgnoreLines.add(currentline);
                    }
                    match = 2;
                }

                if ( line.contains("CFQUERYPARAM_REQ") && match > 1 ) {
                    if (!tmpIgnoreLines.contains(currentline)) {
                        tmpIgnoreLines.add(currentline);
                    }
                    match = 3;
                }

                if ( line.contains("--->") && match > 2 ) {
                    if (!tmpIgnoreLines.contains(currentline)) {
                        tmpIgnoreLines.add(currentline);
                    }
                    if (!tmpIgnoreLines.contains(currentline+1)) {
                        tmpIgnoreLines.add(currentline+1);
                    }
                    ignoreLines.addAll(tmpIgnoreLines);
                    tmpIgnoreLines.clear();
                    match = 0;
                }

                currentline++;

            }
        } catch(Exception e) {
            e.printStackTrace();
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
