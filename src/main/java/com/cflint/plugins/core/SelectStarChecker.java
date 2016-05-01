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
public class SelectStarChecker extends CFLintScannerAdapter {
	final CharSequence selectStar = "select*";
	final String messageCode = "AVOID_SELECT_*_IN_QUERY";
	final String message = "Avoid using 'select *' in a query";
	final String severity = "ERROR";

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	
	}
	
	public void element(final Element element, final Context context, final BugList bugs) {
		String tagName = element.getName();
		if (tagName.equals("cfquery")){
			
			String queryGuts = element.getContent().toString().replaceAll("\\s+","");
			queryGuts = queryGuts.toLowerCase();

			if (queryGuts.contains(selectStar)){
				int beginLine = element.getSource().getRow(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder().setLine(beginLine).setMessageCode(messageCode)
					.setSeverity(severity).setFilename(context.getFilename())
					.setMessage(message)
					.build());
			}
		}
	}		
}


