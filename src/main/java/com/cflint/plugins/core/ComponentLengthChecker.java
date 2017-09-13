package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.BugList;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class ComponentLengthChecker extends LengthChecker {
    private static final int LENGTH_THRESHOLD = 500;

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFCompDeclStatement) {
            final CFCompDeclStatement component = (CFCompDeclStatement) expression;
            final String decompile = component.Decompile(1);
            final String[] lines = decompile.split("\\n");

            checkSize(LENGTH_THRESHOLD, "EXCESSIVE_COMPONENT_LENGTH", context, 1, 0, lines.length, bugs);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String elementName = element.getName();

        if (elementName.equals(CF.CFCOMPONENT)) {
            // this includes whitespace-change it
            final int total = element.getContent().toString().split("\\n").length;

            checkSize(LENGTH_THRESHOLD, "EXCESSIVE_COMPONENT_LENGTH", context, 1, 0, total, bugs);
        }
    }
}
