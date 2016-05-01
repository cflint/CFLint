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
public class ScriptTagChecker extends CFLintScannerAdapter {
	final String message = "Don't use inline <script> tags";
	final String severity = "ERROR";
	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	
	}
	
	//rule: don't use inline javascript in cfm and cfc files
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("script")) {  
			String src = element.getStartTag().toString();
			if (!src.matches(".*src=.*")) {
				int endLine = element.getSource().getRow(element.getEnd()); 
				int begLine = element.getSource().getRow(element.getBegin());
				int total = endLine - begLine;
				bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode("AVOID_USING_INLINE_JS")
						.setSeverity(severity).setFilename(context.getFilename())
						.setMessage(message)
						.build());
			}
		}
	}
}
