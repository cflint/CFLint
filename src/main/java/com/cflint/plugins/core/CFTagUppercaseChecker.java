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
public class CFTagUppercaseChecker extends CFLintScannerAdapter {
	final String messageCode = "CFTAG_SHOULD_BE_UPPERCASE";
	final String severity = "WARNING";
		
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}
	
	//rule: tag should be lowercase or camelCase
	public void element(final Element element, final Context context, final BugList bugs) {
		String tag = element.getStartTag().toString();
		if (tag.substring(1, 3).equalsIgnoreCase("cf")) {
			int index = tag.indexOf(" ");
			if (index == -1) {
				index = tag.indexOf(">");
			}
			String cfTag = tag.substring(1, index);
			String lowerCase = cfTag.toLowerCase();
			/* ensuring the tag is not pure uppercase instead of checking to see if the
			tag is lowercase or camelcase as some camelcase can get hairy e.g. <cfmcGrabURLFromHTTPSite> */
			if (cfTag.equals(lowerCase)) {
				int begLine = element.getSource().getRow(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode(messageCode)
					.setSeverity(severity).setFilename(context.getFilename())
					.setMessage("Tag <" + cfTag + "> should be written in uppercase or camelCase for consistency in code.")
					.build());
			}
		}
	}
}