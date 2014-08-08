package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import com.parser.main.cfscript.CFExpression;
import com.parser.main.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;

@Extension
public class OutputParmMissing implements CFLintScanner {

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		if (// element.getName().equals("cfcomponent") ||
		element.getName().equals("cffunction")) {
			final String outputAttr = element.getAttributeValue("output");
			if (outputAttr == null) {
				final int line = element.getSource().getRow(element.getBegin());
				final int column = element.getSource().getColumn(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setColumn(column).setMessageCode("OUTPUT_ATTR")
						.setSeverity("INFO").setFilename(context.getFilename()).setFunction(context.getFunctionName())
						.setMessage(element.getName() + " should have @output='false' ").build());
			}
		}
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}

}
