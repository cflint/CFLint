package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class CFQueryChecker extends CFLintScannerAdapter {
    final String messageCode = "NEVER_USE_QUERY_IN_CFM";

    // rule: don't allow <cfquery> tag in a .cfm file
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String file = context.getFilename();
        final String ext = file.substring(file.length() - 3, file.length());
        final String tagName = element.getName();
        if (tagName.equals(CF.CFQUERY) && ext.equals("cfm")) {
            final int begLine = element.getSource().getRow(element.getBegin());
            context.addMessage(messageCode, null, this, begLine);
        }
    }
}