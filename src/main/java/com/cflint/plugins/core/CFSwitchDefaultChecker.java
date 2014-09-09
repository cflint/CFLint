package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.CFSwitchStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;

@Extension
public class CFSwitchDefaultChecker implements CFLintScanner {
	final String CFML_TAG_CHECK = "cfswitch";
	final String CFML_TAG_REQUIRED = "cfdefaultcase";
	final String CFSCRIPT_STATEMENT_REQUIRED = "default";
	final String SEVERITY = "WARNING";
	final String MESSAGE_CODE = "NO_DEFAULT_INSIDE_SWITCH";
	final String MESSAGE = "Not having a Default statement defined for a switch could pose potential issues";
	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		// see if expression is cfSwithStatement
		if (expression instanceof CFSwitchStatement) {
			final CFSwitchStatement switchStatement = (CFSwitchStatement)expression;
			String statement = switchStatement.Decompile(0).replace(" ", "").toLowerCase();
			if (!statement.contains(CFSCRIPT_STATEMENT_REQUIRED)) {
				final int line = switchStatement.getLine();
				final int column = switchStatement.getColumn();

				bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setColumn(column)
						.setMessageCode(MESSAGE_CODE).setSeverity(SEVERITY)
						.setFilename(context.getFilename()).setFunction(context.getFunctionName())
						.setMessage(MESSAGE)
						.build());
			}
		}
		// if cfSwitchStatement, then continue to look for cfDefaultStatement
		// if no cfDefaultStatement found, throw a bug.
	}
	
	//rule is: provide a default for switch statements to fall through
	public void element(final Element element, final Context context, final BugList bugs) {
		String tagName = element.getName();
		if (tagName.equalsIgnoreCase(CFML_TAG_CHECK)) {
			boolean isDefault = false;
			for (Element el : element.getChildElements()) {
				// decide if default was provided
				if (el.getName().equalsIgnoreCase(CFML_TAG_REQUIRED)) {
					// default found, so reassign and break
					isDefault = true;
					break;
				}
			}
			if (!isDefault) {	// no default found
			int begLine = element.getSource().getRow(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode(MESSAGE_CODE)
					.setSeverity(SEVERITY).setFilename(context.getFilename())
					.setMessage(MESSAGE)
					.build());
			}
		}
	}
}