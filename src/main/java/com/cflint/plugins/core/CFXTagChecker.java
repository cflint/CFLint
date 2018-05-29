package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;

public class CFXTagChecker extends CFLintScannerAdapter {

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String tagName = element.getName();
        final String cfmlTagCheck = context.getConfiguration().getParameter(this, "tagName");
        final String scope = context.getConfiguration().getParameter(this, "scope");

        if (cfmlTagCheck != null && tagName.matches(cfmlTagCheck)) {
            if (scope == null || scope.equals(CF.COMPONENT) && context.isInComponent()) {
                context.addMessage("AVOID_USING_" + tagName.toUpperCase() + "_TAG", tagName);
            }
        }
    }
}
