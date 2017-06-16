package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class FunctionLengthChecker extends CFLintScannerAdapter {
    final int LENGTH_THRESHOLD = 100;
    final String severity = "INFO";

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            final String decompile = function.Decompile(1);
            final int begLine = function.getLine();
            final String[] lines = decompile.split("\\n");

            checkSize(context, begLine, lines.length, bugs);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String elementName = element.getName();

        if (elementName.equals("cffunction")) {
            // this includes whitespace-change it
            final int begLine = element.getSource().getRow(element.getBegin());
            // int endLine = element.getSource().getRow(element.getEnd());
            final int total = element.getAllStartTags().size();

            checkSize(context, begLine, total, bugs);
        }
    }

    protected void checkSize(final Context context, final int atLine, final int linesLength, final BugList bugs) {
        final String lengthThreshold = getParameter("length");
        int length = LENGTH_THRESHOLD;

        if (lengthThreshold != null) {
            length = Integer.parseInt(lengthThreshold);
        }

        if (linesLength > length) {
            context.addMessage("EXCESSIVE_FUNCTION_LENGTH", Integer.toString(linesLength), this, atLine);
        }
    }
}