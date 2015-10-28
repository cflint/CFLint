package com.cflint.plugins.core;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class CFDebugAttributeChecker extends CFLintScannerAdapter {
	
	public void element(final Element element, final Context context, final BugList bugs) {
		final Attribute debugAttr = element.getAttributes().get("debug");
		if (debugAttr != null) {
			context.addMessage("AVOID_USING_DEBUG_ATTR", null);
		}
	}
}