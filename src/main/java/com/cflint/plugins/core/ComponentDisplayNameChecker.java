package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;

public class ComponentDisplayNameChecker extends CFLintScannerAdapter {

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFCOMPONENT)) {
            final String name = context.getComponentName();
            final String nameAttribute = element.getAttributeValue(CF.NAME);

            if (nameAttribute != null) {
                didYouMeanDisplayName(name, element.getSource().getRow(element.getBegin()), context.offset() + element.getBegin(), context, bugs);
            }
        }
    }

    protected void didYouMeanDisplayName(final String name, final int lineNo, final int offset, final Context context, final BugList bugs) {
        context.addMessage("USE_DISPLAY_NAME", name, this, lineNo, offset);
    }

}
