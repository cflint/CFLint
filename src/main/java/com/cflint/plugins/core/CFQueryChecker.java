package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class CFQueryChecker extends CFLintScannerAdapter {
	final String message = "Don't use <cfquery> in .cfm files. Database should not be coupled with view";
	final String severity = "ERROR";
	final String messageCode = "NEVER_USE_QUERY_IN_CFM";
	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}
	
	//rule: don't allow <cfquery> tag in a .cfm file
	public void element(final Element element, final Context context, final BugList bugs) {
		String file = context.getFilename();
		String ext = file.substring(file.length() - 3, file.length());
		String tagName = element.getName();
		if (tagName.equals("cfquery") && ext.equals("cfm")) {
			int begLine = element.getSource().getRow(element.getBegin());
			bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode(messageCode)
					.setSeverity(severity).setFilename(context.getFilename())
					.setMessage(message)
					.build());
		}
	}
}