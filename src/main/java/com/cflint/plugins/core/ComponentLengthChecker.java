package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Map;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.script.CFCompoundStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

@Extension
public class ComponentLengthChecker extends CFLintScannerAdapter {
	final int LENGTH_THRESHOLD = 500;
	final String severity = "INFO";

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFCompoundStatement) {
			CFCompoundStatement component = (CFCompoundStatement) expression;
			String decompile = component.Decompile(1);
			String[] lines = decompile.split("\\n");

			checkSize(context, lines.length, bugs);
		}
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		String elementName = element.getName();

		if (elementName.equals("cfcomponent")) {
			//this includes whitespace-change it
			int total = element.getAllStartTags().size();

			checkSize(context, total, bugs);
		}
	}

	protected void checkSize(Context context, int linesLength, BugList bugs) {
		String lengthThreshold = getParameter("length");
		int length = LENGTH_THRESHOLD;

		if (lengthThreshold != null) {
			length = Integer.parseInt(lengthThreshold);
		}

		if (linesLength > length) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(1).setMessageCode("EXCESSIVE_COMPONENT_LENGTH")
					.setSeverity(severity).setFilename(context.getFilename())
					.setMessage("Component " + context.getComponentName() + " is " + Integer.toString(linesLength) + " lines. Should be less than " + Integer.toString(length) + " lines.")
					.build());
		}
	}
}