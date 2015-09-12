package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class ComponentHintChecker extends CFLintScannerAdapter {
	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfcomponent")) {
			final String name = element.getAttributeValue("name");
			final String hint = element.getAttributeValue("hint");
			if (hint == null || hint.length() == 0) {
				element.getSource().getRow(element.getBegin());
				element.getSource().getColumn(element.getBegin());
				context.addMessage("COMPONENT_HINT_MISSING", name);
			}
		}
	}

}
