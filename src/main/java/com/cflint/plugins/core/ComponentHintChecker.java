package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class ComponentHintChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	@Override
	public void element(final Element element, final Context context,
			final BugList bugs) {
		if (element.getName().equals("cfcomponent")) {
			final String name = context.getComponentName();
			final String hint = element.getAttributeValue("hint");
			if (hint == null || hint.length() == 0) {
				bugs.add(new BugInfo.BugInfoBuilder()
						.setLine(1)
						.setMessageCode("COMPONENT_HINT_MISSING")
						.setSeverity(severity)
						.setFilename(context.getFilename())
						.setMessage("Component " + name + " is missing a hint.")
						.build());
			}
		}
	}

}
