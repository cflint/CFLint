package com.cflint.plugins.core;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;

public class ArgHintChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name");
			final String hint = element.getAttributeValue("hint");
			if (hint == null || hint.length() == 0) {
				context.addMessage("ARG_HINT_MISSING", name);
			}
		}
	}

}
