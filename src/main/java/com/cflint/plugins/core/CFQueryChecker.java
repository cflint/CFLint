package com.cflint.plugins.core;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class CFQueryChecker extends CFLintScannerAdapter {
	final String message = "Don't use <cfquery> in .cfm files. Database should not be coupled with view";
	final String severity = "ERROR";
	final String messageCode = "NEVER_USE_QUERY_IN_CFM";

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {

	}

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}

	// rule: don't allow <cfquery> tag in a .cfm file
	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		final String file = context.getFilename();
		final String ext = file.substring(file.length() - 3, file.length());
		final String tagName = element.getName();
		if (tagName.equals("cfquery") && ext.equals("cfm")) {
			final int begLine = element.getSource().getRow(element.getBegin());
			bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode(messageCode).setSeverity(severity)
					.setFilename(context.getFilename()).setMessage(message).build());
		}
	}
}