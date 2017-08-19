package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;

public class OutputParmMissing extends CFLintScannerAdapter {

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (// element.getName().equals(CF.CFCOMPONENT) ||
        element.getName().equals(CF.CFFUNCTION)) {
            final String outputAttr = element.getAttributeValue(CF.OUTPUT);
            if (outputAttr == null) {
                context.addMessage("OUTPUT_ATTR", element.getAttributeValue(CF.NAME));
            }
        }
    }
}
