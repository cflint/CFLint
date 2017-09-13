package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.BugList;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class FunctionLengthChecker extends LengthChecker {
    private static final int LENGTH_THRESHOLD = 100;

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            final String decompile = function.Decompile(1);
            final int begLine = function.getLine();
            final int offset = function.getOffset() + context.offset();
            final String[] lines = decompile.split("\\n");

            checkSize(LENGTH_THRESHOLD, "EXCESSIVE_FUNCTION_LENGTH", context, begLine, offset, lines.length, bugs);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String elementName = element.getName();

        if (elementName.equals(CF.CFFUNCTION)) {
            // this includes whitespace-change it
            final int begLine = element.getSource().getRow(element.getBegin());
            final int offset = element.getBegin();
            final int total = element.getAllStartTags().size();

            checkSize(LENGTH_THRESHOLD, "EXCESSIVE_FUNCTION_LENGTH", context, begLine, offset, total, bugs);
        }
    }

}
