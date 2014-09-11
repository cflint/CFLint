package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;


@Extension
public class CFDumpChecker implements CFLintScanner {
	final String cfmlTagCheck = "cfdump";
	final String severity = "WARNING";
	final String messageCode = "AVOID_USING_" + cfmlTagCheck.toUpperCase() + "_TAG";
	final String message = "Avoid Leaving<" + 
							cfmlTagCheck +
							"> tags in committed code. Debug information should be ommited from release code";
	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}
	
	public void element(final Element element, final Context context, final BugList bugs) {
		String tagName = element.getName();
		if (tagName.contains(cfmlTagCheck)){
			int begLine = element.getSource().getRow(element.getBegin());
			bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode(messageCode)
				.setSeverity(severity).setFilename(context.getFilename())
				.setMessage(message)
				.build());
		}
	}
}

			

				
			