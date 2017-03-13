package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class SelectStarChecker extends CFLintScannerAdapter {
    final CharSequence selectStar = "select*";
    
    // ".*[^\\(\\*]+(\\s*\\*\\s*)[^\\w]+"

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {

    }

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {

    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String tagName = element.getName();
        if (tagName.equals("cfquery")) {

            String queryGuts = element.getContent().toString().replaceAll("\\s+", "");
            queryGuts = queryGuts.toLowerCase();

            if (queryGuts.contains(selectStar)) {
                context.addMessage("SQL_SELECT_STAR", null);
            }
        }
    }
}
