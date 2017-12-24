package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class ArrayNewChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFExpressionStatement) {
            final String code = ((CFExpressionStatement) expression).getExpression().Decompile(0);

            if (code.toLowerCase().contains("arraynew(1)")) {
                context.addMessage("AVOID_USING_ARRAYNEW", null);
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFSET)) {
            final String content = element.getStartTag().getTagContent().toString();

            if (content.toLowerCase().contains("arraynew(1)")) {
                context.addMessage("AVOID_USING_ARRAYNEW", null);
            }
        }
    }

}
