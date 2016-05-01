package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class CFXTagChecker extends CFLintScannerAdapter {

	@Override
	public void element(final Element element, final Context context,
			final BugList bugs) {
		final String tagName = element.getName();
		final String cfmlTagCheck = getParameter("tagName");
		final String scope = getParameter("scope");

		if (tagName.matches(cfmlTagCheck)) {
			if (scope == null || scope.equals("component")
					&& context.isInComponent()) {
				context.addMessage("AVOID_USING_" + tagName.toUpperCase()
						+ "_TAG", tagName);
			}
		}
	}
}
