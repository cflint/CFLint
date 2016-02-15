package com.cflint.plugins.core;

import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFVarDeclExpression;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

public class UnusedLocalVarChecker extends CFLintScannerAdapter {
	final String severity = "INFO";

	protected CFScopes scopes = new CFScopes();
	protected Map<String, Boolean> localVariables = new HashMap<String, Boolean>();
	protected Map<String, Integer> variableLineNo = new HashMap<String, Integer>();

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFullVarExpression) {
			CFExpression variable = ((CFFullVarExpression) expression).getExpressions().get(0);
			if (variable instanceof CFIdentifier) {
				String name = ((CFIdentifier) variable).getName();
				if (!scopes.isCFScoped(name) || scopes.isLocalScoped(name)) {
					localVariables.put(name, true);
				}
			}
		}
		else if (expression instanceof CFVarDeclExpression) {
			final String name = ((CFVarDeclExpression) expression).getName();
			final int lineNo = expression.getLine() + context.startLine() - 1;
			addLocalVariable(name, lineNo);
		}
		else if (expression instanceof CFIdentifier) {
			localVariables.put(((CFIdentifier) expression).getName(), true);
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
	public void startFunction(Context context, BugList bugs) {
		localVariables.clear();
		variableLineNo.clear();
	}

	@Override	
	public void endFunction(Context context, BugList bugs) {
		// sort by line number
		final List<BugInfo> presortbugs = new ArrayList<BugInfo>();
		for (Map.Entry<String, Boolean> variable : localVariables.entrySet()) {
			Boolean used = variable.getValue();
	    	if (!used) {
	    		final String name = variable.getKey();
	    		final Integer lineNo = variableLineNo.get(name);
	    		presortbugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("UNUSED_LOCAL_VARIABLE")
					.setSeverity(severity).setFilename(context.getFilename()).setFunction(context.getFunctionName())
					.setMessage("Local variable " + name + " is not used in function " + context.getFunctionName() + ", consider removing it.")
					.setVariable(name)
					.build());
	    	}
	    }
		// Sort the bugs by line/col before adding to the list of bugs.
		Collections.sort(presortbugs);
		for(BugInfo bugInfo : presortbugs ){
			bugs.add(bugInfo);
		}
	}

}
