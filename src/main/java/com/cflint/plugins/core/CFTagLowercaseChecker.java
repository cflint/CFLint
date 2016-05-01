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
public class CFTagLowercaseChecker extends CFLintScannerAdapter {
	final String messageCode = "CFTAG_SHOULD_BE_LOWERCASE";
	final String severity = "WARNING";

	public void expression(final CFExpression expression,
			final Context context, final BugList bugs) {

	}

	public void expression(final CFScriptStatement expression,
			final Context context, final BugList bugs) {
	}

	// rule: tag should be lowercase or camelCase
	public void element(final Element element, final Context context,
			final BugList bugs) {
		String tag = element.getStartTag().toString();
		if (tag.substring(1, 3).equalsIgnoreCase("cf")) {
			int index = tag.indexOf(" ");
			if (index == -1) {
				index = tag.indexOf(">");
			}
			String cfTag = tag.substring(1, index);
			String upperCase = cfTag.toUpperCase();
			/*
			 * ensuring the tag is not pure uppercase instead of checking to see
			 * if the tag is lowercase or camelcase as some camelcase can get
			 * hairy e.g. <cfmcGrabURLFromHTTPSite>
			 */
			if (cfTag.equals(upperCase)) {
				int begLine = element.getSource().getRow(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder()
						.setLine(begLine)
						.setMessageCode(messageCode)
						.setSeverity(severity)
						.setFilename(context.getFilename())
						.setMessage(
								"Tag <"
										+ cfTag
										+ "> should be written in lowercase or camelCase for consistency in code.")
						.build());
			}
		}
	}
}