package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class ComponentDisplayNameChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	@Override
	public void element(final Element element, final Context context,
			final BugList bugs) {
		if (element.getName().equals("cfcomponent")) {
			final String name = context.getComponentName();
			final String nameAttribute = element.getAttributeValue("name");

			if (nameAttribute != null) {
				didYouMeanDisplayName(name, context, bugs);
			}
		}
	}

	protected void didYouMeanDisplayName(String name, Context context,
			BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder()
				.setLine(1)
				.setMessageCode("USE_DISPLAY_NAME")
				.setSeverity(severity)
				.setFilename(context.getFilename())
				.setMessage(
						"Component "
								+ name
								+ " is has a name attribute perhaps you meant to use displayName?")
				.build());
	}

}
