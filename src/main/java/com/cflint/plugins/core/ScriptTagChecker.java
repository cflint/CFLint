package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

// Deprecate?
@Extension
public class ScriptTagChecker extends CFLintScannerAdapter {
	final String message = "Don't use inline <script> tags";
	final String severity = "ERROR";

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {

	}

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {

	}

	// rule: don't use inline javascript in cfm and cfc files
	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("script")) {
			final String src = element.getStartTag().toString();
			if (!src.matches(".*src=.*")) {
				context.addMessage("AVOID_USING_INLINE_JS", null);
			}
		}
	}
}
