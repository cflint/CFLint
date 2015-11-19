package com.cflint.plugins.core;

import java.util.Map;
import java.util.LinkedHashMap;

import net.htmlparser.jericho.Element;

import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFVarDeclExpression;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class UnusedArgumentChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	protected Map<String, Boolean> methodArguments = new LinkedHashMap<String, Boolean>();
	protected Map<String, Integer> argumentLineNo = new LinkedHashMap<String, Integer>();

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name");
			methodArguments.put(name, false);
			setArgumentLineNo(name, context.startLine());
		}
	}

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			for (final CFFunctionParameter argument : function.getFormals()) {
				final String name = argument.getName();
				methodArguments.put(name, false);
				setArgumentLineNo(name, function.getLine()); // close enough?
			}
		}
	}

	protected void setArgumentLineNo(final String argument, final Integer lineNo) {
		if (argumentLineNo.get(argument) == null) {
			argumentLineNo.put(argument, lineNo);
		}
	}

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFullVarExpression) {
			CFExpression variable = ((CFFullVarExpression) expression).getExpressions().get(0);
			if (variable instanceof CFIdentifier) {
				String name = ((CFIdentifier) variable).getName();
				if (methodArguments.get(name) != null) {
					methodArguments.put(name, true);
				}
			}
		}
		else if (expression instanceof CFIdentifier) {
			String name = ((CFIdentifier) expression).getName();
			if (methodArguments.get(name) != null) {
				methodArguments.put(name, true);
			}
		}
	}

	@Override	
	public void startFunction(Context context, BugList bugs) {
		methodArguments.clear();
	}

	@Override	
	public void endFunction(Context context, BugList bugs) {
		// TODO perhaps sort by line number?
		for (Map.Entry<String, Boolean> method : methodArguments.entrySet()) {
			Boolean used = method.getValue();
	    	if (!used) {
	    		final String name = method.getKey();
	    		final Integer lineNo = argumentLineNo.get(name);
				bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("UNUSED_METHOD_ARGUMENT")
					.setSeverity(severity).setFilename(context.getFilename())
					.setMessage("Argument " + name + " is not used in function " + context.getFunctionName() + ", consider removing it.")
					.build());
	    	}
	    }
	}

}
