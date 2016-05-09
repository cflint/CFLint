package com.cflint.plugins.core;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;

public class FunctionHintChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cffunction")) {
			final String name = element.getAttributeValue("name");
			final String hint = element.getAttributeValue("hint");
			if (hint == null || hint.length() == 0) {
				int begLine = element.getSource().getRow(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode("FUNCTION_HINT_MISSING")
					.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
					.setMessage("Function " + name + " is missing a hint.")
					.build());
			}
		}
	}

}
