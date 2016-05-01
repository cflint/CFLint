package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;
import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class ArrayNewChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	@Override
	public void expression(final CFScriptStatement expression,
			final Context context, final BugList bugs) {
		if (expression instanceof CFExpressionStatement) {
			String code = ((CFExpressionStatement) expression).getExpression()
					.Decompile(0);
			int lineNo = ((CFExpressionStatement) expression).getLine()
					+ context.startLine() - 1;

			if (code.toLowerCase().contains("arraynew(1)")) {
				arrayNew(lineNo, context, bugs);
			}
		}
	}

	@Override
	public void element(final Element element, final Context context,
			final BugList bugs) {
		if (element.getName().equals("cfset")) {
			String content = element.getStartTag().getTagContent().toString();
			int lineNo = element.getSource().getRow(element.getBegin());

			if (content.toLowerCase().contains("arraynew(1)")) {
				arrayNew(lineNo, context, bugs);
			}
		}
	}

	protected void arrayNew(final int lineNo, final Context context,
			final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder()
				.setLine(lineNo)
				.setMessageCode("AVOID_USING_ARRAYNEW")
				.setSeverity(severity)
				.setFilename(context.getFilename())
				.setMessage(
						"ArrayNew statement at line "
								+ lineNo
								+ ". Use implict array construction instead (= []).")
				.build());
	}
}