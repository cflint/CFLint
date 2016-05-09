package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.List;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.CFSCRIPTLexer;
import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import ro.fortsoft.pf4j.Extension;

@Extension
public class CFCompareVsAssignChecker extends CFLintScannerAdapter {
	final String severity = "INFO";
	
	List<Integer> TOKENS = Arrays.asList(CFSCRIPTLexer.EQUALSEQUALSOP,
			CFSCRIPTLexer.LT,
			CFSCRIPTLexer.LTE,
			CFSCRIPTLexer.GT,
			CFSCRIPTLexer.GTE,
			CFSCRIPTLexer.OR,
			CFSCRIPTLexer.OROPERATOR,
			CFSCRIPTLexer.EQV,
			CFSCRIPTLexer.XOR,
			CFSCRIPTLexer.AND,
			CFSCRIPTLexer.ANDOPERATOR,
			CFSCRIPTLexer.EQ,
			CFSCRIPTLexer.NEQ,
			CFSCRIPTLexer.CONTAINS);

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {	
		if(expression instanceof CFExpressionStatement){
			final CFExpressionStatement exprStatement = (CFExpressionStatement)expression;
			if(exprStatement.getExpression() instanceof CFBinaryExpression){
				CFBinaryExpression binaryExpression = (CFBinaryExpression) exprStatement.getExpression();
				if(TOKENS.contains(binaryExpression.getToken().getType())){
					context.addMessage("COMPARE_INSTEAD_OF_ASSIGN", binaryExpression.getToken().getText());
				}
			}
		}
	}

	protected void noNeedtoUseCreateObject(final int lineNo, final Context context, final BugList bugs) {
		bugs.add(new BugInfo.BugInfoBuilder().setLine(lineNo).setMessageCode("AVOID_USING_CREATEOBJECT")
			.setSeverity(severity).setFilename(context.getFilename())
			.setMessage("CreateObject statement at line " + lineNo + ". Use createObject(path_to_component) or even better new path_to_component().")
			.build());
	}
}