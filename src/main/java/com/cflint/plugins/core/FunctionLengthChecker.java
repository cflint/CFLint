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
public class FunctionLengthChecker extends CFLintScannerAdapter {
	final int LENGTH_THRESHOLD = 50;
	final String severity = "INFO";

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			String decompile = function.Decompile(1);
			final int begLine = function.getLine();
			String[] lines = decompile.split("\\n");
			CFCompoundStatement body = (CFCompoundStatement) function.getBody();

			checkSize(context, begLine, lines.length, bugs);
		}
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		String elementName = element.getName();
		if (elementName.equals("cffunction")) {
			//this includes whitespace-change it
			int begLine = element.getSource().getRow(element.getBegin());
			//int endLine = element.getSource().getRow(element.getEnd()); 
			int total = element.getAllStartTags().size();

			checkSize(context, begLine, total, bugs);
		}
	}

	protected void checkSize(Context context, int atLine, int linesLength, BugList bugs) {
		String lengthThreshold = getParameter("length");
		int length = LENGTH_THRESHOLD;

		if (lengthThreshold != null) {
			length = Integer.parseInt(lengthThreshold);
		}

		if (linesLength > length) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(atLine).setMessageCode("EXCESSIVE_FUNCTION_LENGTH")
					.setSeverity(severity).setFilename(context.getFilename())
					.setMessage("Function is " + linesLength + " lines. Should be less than " + LENGTH_THRESHOLD + " lines.")
					.build());
		}
	}
}