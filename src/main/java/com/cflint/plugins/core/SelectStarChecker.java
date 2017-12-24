package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class SelectStarChecker extends CFLintScannerAdapter {
    private static final CharSequence selectStar = "select*";

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String tagName = element.getName();
        if (tagName.equals(CF.CFQUERY)) {

            String queryGuts = element.getContent().toString().replaceAll("\\s+", "");
            queryGuts = queryGuts.toLowerCase();

            if (queryGuts.contains(selectStar)) {
                context.addMessage("SQL_SELECT_STAR", null);
            }
        }
    }
}
