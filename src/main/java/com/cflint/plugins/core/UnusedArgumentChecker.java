package com.cflint.plugins.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class UnusedArgumentChecker extends CFLintScannerAdapter {

	//Use linked hash map to preserve the order of the elements.
	protected Map<String, Boolean> methodArguments = new LinkedHashMap<String, Boolean>();
	protected Map<String, Integer> argumentLineNo = new HashMap<String, Integer>();

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name") != null ? 
					element.getAttributeValue("name").toLowerCase() : "";
			methodArguments.put(name, false);
			setArgumentLineNo(name, context.startLine());
		}
	}

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			for (final CFFunctionParameter argument : function.getFormals()) {
				final String name = argument.getName().toLowerCase(); // CF variable names are not case sensitive
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

	protected void useIdentifier(CFIdentifier identifier) {
		String name = identifier.getName().toLowerCase();
		if (name.equals("arguments")) {
			name = identifier.Decompile(0).toLowerCase().replace("arguments.", ""); // TODO better way of doing this?
		}
		if (methodArguments.get(name) != null) {
			methodArguments.put(name, true);
		}
	}

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFullVarExpression) {
			CFExpression variable = ((CFFullVarExpression) expression).getExpressions().get(0);
			if (variable instanceof CFIdentifier) {
				useIdentifier((CFIdentifier) expression);
			}
		}
		else if (expression instanceof CFIdentifier) {
			useIdentifier((CFIdentifier) expression);		
		}
	}

	@Override	
	public void startFunction(Context context, BugList bugs) {
		methodArguments.clear();
	}

	@Override	
	public void endFunction(Context context, BugList bugs) {
		// sort by line number
		for (Map.Entry<String, Boolean> method : methodArguments.entrySet()) {
			Boolean used = method.getValue();
	    	if (!used) {
	    		final String name = method.getKey();
	    		context.addMessage("UNUSED_METHOD_ARGUMENT", name,argumentLineNo.get(name));
	    	}
	    }
	}

}
