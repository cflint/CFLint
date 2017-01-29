package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;

public class ComponentDisplayNameChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfcomponent")) {
			final String name = context.getComponentName();
			final String nameAttribute = element.getAttributeValue("name");

			if (nameAttribute != null) {
				didYouMeanDisplayName(name, context, bugs);
			}
		}
	}

	protected void didYouMeanDisplayName(final String name, final Context context, final BugList bugs) {
	    context.addMessage("USE_DISPLAY_NAME", name, this, 1);
	}

}
