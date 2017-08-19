package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.Levels;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;

public class ComponentDisplayNameChecker extends CFLintScannerAdapter {
    private final Levels severity = Levels.INFO;

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFCOMPONENT)) {
            final String name = context.getComponentName();
            final String nameAttribute = element.getAttributeValue(CF.NAME);

            if (nameAttribute != null) {
                didYouMeanDisplayName(name, context, bugs);
            }
        }
    }

    protected void didYouMeanDisplayName(final String name, final Context context, final BugList bugs) {
        context.addMessage("USE_DISPLAY_NAME", name, this, 1);
    }

}
