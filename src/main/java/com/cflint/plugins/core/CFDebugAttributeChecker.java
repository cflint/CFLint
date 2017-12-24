package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class CFDebugAttributeChecker extends CFLintScannerAdapter {

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final Attributes attributes = element.getAttributes();
        if (attributes == null) {
            return;
        }
        final Attribute debugAttr = attributes.get(CF.DEBUG);
        if (debugAttr != null) {
            if(!debugAttr.hasValue() || (!debugAttr.getValue().equalsIgnoreCase("no") && !debugAttr.getValue().equalsIgnoreCase("false")))
                context.addMessage("AVOID_USING_DEBUG_ATTR", null);
        }
        if (element.getName().equalsIgnoreCase(CF.CFSETTING)) {
            final Attribute showDebugOutputAttr = element.getAttributes().get("showDebugOutput");
            if (showDebugOutputAttr != null) {
                if ("Yes".equalsIgnoreCase(showDebugOutputAttr.getValue())
                        || "true".equalsIgnoreCase(showDebugOutputAttr.getValue())) {
                    context.addMessage("AVOID_USING_CFSETTING_DEBUG", null);
                }
            }

        }
    }
}
