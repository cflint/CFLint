package com.cflint.plugins.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
import net.htmlparser.jericho.Attribute;

public class UnusedArgumentChecker extends CFLintScannerAdapter {

	// Use linked hash map to preserve the order of the elements.
	protected Map<String, Boolean> methodArguments = new LinkedHashMap<String, Boolean>();
	protected Map<String, Integer> argumentLineNo = new HashMap<String, Integer>();

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name") != null
					? element.getAttributeValue("name").toLowerCase() : "";
			methodArguments.put(name, false);
			setArgumentLineNo(name, context.startLine());
		} else {
			final String code = element.toString().replace(element.getName(), "");
			for (final Map.Entry<String, Boolean> method : methodArguments.entrySet()) {
				final String argName = method.getKey();
				final Boolean used = method.getValue();
				if(!used){
					methodArguments.put(argName, isCheck(code, argName));
				}
			}
		}
	}
	

	private boolean isCheck(String content, final String name){
		boolean checked = false;
		content = content.replace(" ", "").replace("'", "\"").toLowerCase();
		boolean structKeyCheck = (content.contains("structkeyexists(arguments,\""+name+"\""));
		boolean isDefinedCheck = (content.contains("isdefined(\"arguments."+name+"\""));
		boolean isUsedCheck = (content.contains("arguments."+name)) || (content.contains("arguments[\""+name+"\"]"));
		final Pattern valid = Pattern.compile(".*\\(\\s*.+\\s*=\\s*(arguments)\\s*\\).*");
		boolean isPassedCheck = valid.matcher(content).matches();
		checked = structKeyCheck || isDefinedCheck || isUsedCheck || isPassedCheck;
		return checked;
	}

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			for (final CFFunctionParameter argument : function.getFormals()) {
				final String name = argument.getName().toLowerCase(); // CF
																		// variable
																		// names
																		// are
																		// not
																		// case
																		// sensitive
				methodArguments.put(name, false);
				setArgumentLineNo(name, function.getLine()); // close enough?
			}
		}
	}
	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		final String code = expression.toString();
		if (expression instanceof CFFullVarExpression) {
			final CFExpression variable = ((CFFullVarExpression) expression).getExpressions().get(0);
			if (variable instanceof CFIdentifier) {
				useIdentifier((CFIdentifier) expression);
			}
		} else if (expression instanceof CFIdentifier) {
			useIdentifier((CFIdentifier) expression);
		} else {
			
			for (final Map.Entry<String, Boolean> method : methodArguments.entrySet()) {
				final String argName = method.getKey();
				final Boolean used = method.getValue();
				if(!used){
					methodArguments.put(argName, isCheck(code, argName));
				}
			}
		}
	}
	

	protected void setArgumentLineNo(final String argument, final Integer lineNo) {
		if (argumentLineNo.get(argument) == null) {
			argumentLineNo.put(argument, lineNo);
		}
	}

	protected void useIdentifier(final CFIdentifier identifier) {
		String name = identifier.getName().toLowerCase();
		if (name.equals("arguments")) {
			name = identifier.Decompile(0).toLowerCase();
			name = name.replace("['", ".").replace("']", "");
			if(name.split("\\.").length > 1){
				name = name.split("\\.")[1]; // TODO
																					// better
																					// way
																					// of
																					// doing
																					// this?
			}
		}
		if (methodArguments.get(name) != null) {
			methodArguments.put(name, true);
		}
	}

	

	@Override
	public void startFunction(final Context context, final BugList bugs) {
		methodArguments.clear();
		argumentLineNo.clear();
	}

	@Override
	public void endFunction(final Context context, final BugList bugs) {
		// sort by line number
		for (final Map.Entry<String, Boolean> method : methodArguments.entrySet()) {
			final Boolean used = method.getValue();
			if (!used) {
				final String name = method.getKey();
				context.addMessage("UNUSED_METHOD_ARGUMENT", name, argumentLineNo.get(name));
			}
		}
	}

}
