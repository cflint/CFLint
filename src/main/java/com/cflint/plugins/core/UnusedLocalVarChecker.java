package com.cflint.plugins.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFMember;
import cfml.parsing.cfscript.CFVarDeclExpression;
import net.htmlparser.jericho.Element;

public class UnusedLocalVarChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	protected CFScopes scopes = new CFScopes();
	protected Map<String, Boolean> localVariables = new HashMap<String, Boolean>();
	protected Map<String, Integer> variableLineNo = new HashMap<String, Integer>();

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFullVarExpression) {
			final CFExpression variable = ((CFFullVarExpression) expression).getExpressions().get(0);
			if (variable instanceof CFIdentifier) {
				final String name = ((CFIdentifier) variable).getName();
				if (!scopes.isCFScoped(name) || scopes.isLocalScoped(name)) {
					localVariables.put(name, true);
				}
			}
			for(CFExpression subexpr: ((CFFullVarExpression) expression).getExpressions()){
				if(subexpr instanceof CFMember){
					CFMember memberExpr = (CFMember) subexpr;
					if(memberExpr.getExpression() != null) {
						final String name = memberExpr.getExpression().toString();
						if (!scopes.isCFScoped(name) || scopes.isLocalScoped(name)) {
							localVariables.put(name, true);
						}
					}
				}
			}
		} else if (expression instanceof CFVarDeclExpression) {
			final String name = ((CFVarDeclExpression) expression).getName();
			final int lineNo = expression.getLine() + context.startLine() - 1;
			addLocalVariable(name, lineNo);
		} else if (expression instanceof CFIdentifier) {
			localVariables.put(((CFIdentifier) expression).getName(), true);
		}
	}
	
	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		final String elementName = element.getName();
		//final int begLine = element.getSource().getRow(element.getBegin());
		String varName = "";

		if (elementName.equals("cfquery")) {
			if(element.getAttributeValue("name") != null){
				varName = element.getAttributeValue("name") != null ? element.getAttributeValue("name") : "";
				localVariables.put(varName, true);
			}
		} else if (elementName.equals("cfinvoke")) {
			if(element.getAttributeValue("returnvariable") != null){
				varName = element.getAttributeValue("returnvariable") != null ? element.getAttributeValue("returnvariable") : "";
				localVariables.put(varName, true);
			}
		} else if (elementName.equals("cfloop")) {
			if(element.getAttributeValue("index") != null || element.getAttributeValue("item") != null){
				varName = element.getAttributeValue("index") != null ? element.getAttributeValue("index") : (element.getAttributeValue("item") != null ? element.getAttributeValue("item") : "");
				localVariables.put(varName, true);
			}
		} else if (elementName.equals("cfsavecontent")) {
			if(element.getAttributeValue("variable") != null){
				varName = element.getAttributeValue("variable") != null ? element.getAttributeValue("variable") : "";
				localVariables.put(varName, true);
			}
		}
	}

	protected void addLocalVariable(final String variable, final Integer lineNo) {
		if (localVariables.get(variable) == null) {
			localVariables.put(variable, false);
			setLocalVariableLineNo(variable, lineNo);
		}
	}

	protected void setLocalVariableLineNo(final String variable, final Integer lineNo) {
		if (variableLineNo.get(variable) == null) {
			variableLineNo.put(variable, lineNo);
		}
	}

	@Override
	public void startFunction(final Context context, final BugList bugs) {
		localVariables.clear();
		variableLineNo.clear();
	}

	@Override
	public void endFunction(final Context context, final BugList bugs) {
		// sort by line number
		final List<BugInfo> presortbugs = new ArrayList<BugInfo>();
		for (final Map.Entry<String, Boolean> variable : localVariables.entrySet()) {
			final Boolean used = variable.getValue();
			if (!used) {
				final String name = variable.getKey();
				final Integer lineNo = variableLineNo.get(name);
				presortbugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("UNUSED_LOCAL_VARIABLE")
						.setSeverity(severity).setFilename(context.getFilename())
						.setFunction(context.getFunctionName()).setMessage("Local variable " + name
								+ " is not used in function " + context.getFunctionName() + ", consider removing it.")
						.setVariable(name).build());
			}
		}
		// Sort the bugs by line/col before adding to the list of bugs.
		Collections.sort(presortbugs);
		for (final BugInfo bugInfo : presortbugs) {
			bugs.add(bugInfo);
		}
	}

}
