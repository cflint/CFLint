package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

public class NestedCFOutput extends CFLintScannerAdapter {

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfoutput")) {
			final Element parent = CFTool.getNamedParent(element, "cfoutput");
			if (parent != null && parent.getAttributeValue("query") != null
					&& parent.getAttributeValue("group") == null) {
				element.getSource().getRow(element.getBegin());
				element.getSource().getColumn(element.getBegin());
				context.addMessage("NESTED_CFOUTPUT", "");
			}
		}
	}

}
