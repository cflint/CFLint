package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class OutputParmMissing extends CFLintScannerAdapter {

	@Override
	public void element(final Element element, final Context context,
			final BugList bugs) {
		if (// element.getName().equals("cfcomponent") ||
		element.getName().equals("cffunction")) {
			final String outputAttr = element.getAttributeValue("output");
			if (outputAttr == null) {
				context.addMessage("OUTPUT_ATTR",
						element.getAttributeValue("name"));
			}
		}
	}
}
