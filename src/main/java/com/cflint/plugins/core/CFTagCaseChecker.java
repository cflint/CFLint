package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class CFTagCaseChecker extends CFLintScannerAdapter {
	final String messageCode = "CFTAG_PREFERRED_CASE";
	
	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {

	}

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}

	// rule: tag should be lowercase or camelCase
	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
	    boolean encourageUpper = true;
        if (getParameter("PreferCase") != null) {
            try {
                encourageUpper = "upper".equalsIgnoreCase(getParameter("PreferCase"));
            } catch (final Exception e) {
            }
        }	    
		final String tag = element.getStartTag().toString();
		if (tag.substring(1, 3).equalsIgnoreCase("cf")) {
			int index = tag.indexOf(" ");
			if (index == -1) {
				index = tag.indexOf(">");
			}
			final String cfTag = tag.substring(1, index);
			final String nonPreferredCase = encourageUpper?cfTag.toLowerCase():cfTag.toUpperCase();
			/*
			 * ensuring the tag is not pure uppercase instead of checking to see
			 * if the tag is lowercase or camelcase as some camelcase can get
			 * hairy e.g. <cfmcGrabURLFromHTTPSite>
			 */
			if (cfTag.equals(nonPreferredCase)) {
				final int begLine = element.getSource().getRow(element.getBegin());
				context.addMessage(messageCode, cfTag, this, begLine);
				//messageText = "Tag <${variable}> should be written in lowercase or camelCase for consistency in code.";
			}
		}
	}
}