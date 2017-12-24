package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

import net.htmlparser.jericho.Element;

public class NestedCFOutput extends CFLintScannerAdapter {

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFOUTPUT)) {
            final Element parent = CFTool.getNamedParent(element, CF.CFOUTPUT);
            if (parent != null) {
                if (parent.getAttributeValue(CF.GROUP) == null && anyContainingCFOutputHasQuery(parent)) {
                    element.getSource().getRow(element.getBegin());
                    element.getSource().getColumn(element.getBegin());
                    context.addMessage("NESTED_CFOUTPUT", "");
                }
            }
        }
    }

    private final boolean anyContainingCFOutputHasQuery(final Element element) {
        if (element == null) {
            return false;
        }
        if (element.getAttributeValue(CF.QUERY) != null) {
            return true;
        }
        return anyContainingCFOutputHasQuery(CFTool.getNamedParent(element, CF.CFOUTPUT));
    }

}
