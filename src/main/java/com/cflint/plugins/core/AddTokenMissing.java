package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;

public class AddTokenMissing extends CFLintScannerAdapter {

    
    /** 
     * @param element element
     * @param context context
     * @param bugs bugs
     */
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFLOCATION)) {
            final String outputAttr = element.getAttributeValue(CF.ADDTOKEN);
            if (outputAttr == null || outputAttr.toLowerCase() == "yes" || outputAttr.toLowerCase() == "true") {
                context.addMessage("ADDTOKEN_ATTR", element.getAttributeValue(CF.URL));
            }
        }
    }
}
