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
	final int LENGTH_THRESHOLD = 100;
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
			if (lines.length > LENGTH_THRESHOLD) {
				bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode("EXCESSIVE_FUNCTION_LENGTH")
						.setSeverity(severity).setFilename(context.getFilename())
						.setMessage("Function is " + lines.length + " lines. Should be less than " + LENGTH_THRESHOLD + " lines.")
						.build());
			}
		}
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		String elementName = element.getName();
		if (elementName.equals("cffunction")) {
			//this includes whitespace-change it
			int begLine = element.getSource().getRow(element.getBegin());
			//int endLine = element.getSource().getRow(element.getEnd()); 
			int total = element.getAllStartTags().size();
			if (total > LENGTH_THRESHOLD) {
				bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode("EXCESSIVE_FUNCTION_LENGTH")
						.setSeverity(severity).setFilename(context.getFilename())
						.setMessage("Function is " + total + " lines. Should be less than " + LENGTH_THRESHOLD + " lines.")
						.build());
			}
		}
	}
}