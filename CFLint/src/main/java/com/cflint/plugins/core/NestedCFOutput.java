package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import com.parser.main.cfscript.CFExpression;
import com.parser.main.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

@Extension
public class NestedCFOutput implements CFLintScanner {

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfoutput")) {
			final Element parent = CFTool.getNamedParent(element, "cfoutput");
			if (parent != null && parent.getAttributeValue("query") != null
					&& parent.getAttributeValue("group") == null) {
				final int line = element.getSource().getRow(element.getBegin());
				final int column = element.getSource().getColumn(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setColumn(column).setMessageCode("NESTED_CFOUTPUT")
						.setSeverity("ERROR").setFilename(context.getFilename()).setFunction(context.getFunctionName())
						.setMessage("Nested CFOutput, outer CFOutput has @query.").build());
			}
		}
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}

}
