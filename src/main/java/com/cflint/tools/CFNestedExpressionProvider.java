package com.cflint.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFNestedExpression;
import cfml.parsing.cfscript.CFStringExpression;
import cfml.parsing.cfscript.CFStructElementExpression;
import cfml.parsing.cfscript.CFStructExpression;
import cfml.parsing.cfscript.CFUnaryExpression;
import cfml.parsing.cfscript.CFVarDeclExpression;

public class CFNestedExpressionProvider {

	final CFExpression expression;
	
	protected CFNestedExpressionProvider(CFExpression expression) {
		this.expression = expression;
	}

	public static CFNestedExpressionProvider createInstance(CFExpression expression){
		return new CFNestedExpressionProvider(expression);
	}
	
	public List<CFExpression> getChildExpressions(){
		if (expression instanceof CFUnaryExpression) {
			return wrap(((CFUnaryExpression) expression).getSub());
		} else if (expression instanceof CFNestedExpression) {
			return wrap(((CFNestedExpression) expression).getSub());
		} else if (expression instanceof CFAssignmentExpression) {
			return wrap(((CFAssignmentExpression) expression).getRight());
		} else if (expression instanceof CFBinaryExpression) {
			return wrap(((CFBinaryExpression) expression).getLeft(),((CFBinaryExpression) expression).getRight());
		} else if (expression instanceof CFFunctionExpression) {
			final CFFunctionExpression cfFunctionExpr = (CFFunctionExpression) expression;
			ArrayList<CFExpression> retval = new ArrayList<CFExpression>();
			for (final CFExpression expr : cfFunctionExpr.getArgs()) {
				if (expr instanceof CFAssignmentExpression) {
					//Only the right hand side of 'assignments' -- these are named parameters.
					retval.add(((CFAssignmentExpression) expr).getRight());
				} else {
					retval.add(expr);
				}
			}
			return retval;
		} else if (expression instanceof CFIdentifier) {
			ArrayList<CFExpression> retval = new ArrayList<CFExpression>();
			if (expression instanceof CFFullVarExpression) {
				final CFFullVarExpression fullVarExpression = (CFFullVarExpression) expression;
				for (final CFExpression expr : fullVarExpression.getExpressions()) {
					if (expr instanceof CFFunctionExpression) {
						retval.add(expr);
					}
				}
			}
			return retval;
		} else if (expression instanceof CFVarDeclExpression) {
			return wrap(((CFVarDeclExpression) expression).getInit());
		} else if (expression instanceof CFStringExpression) {
			final CFStringExpression stringExpression = (CFStringExpression) expression;
			return stringExpression.getSubExpressions();
		} else if (expression instanceof CFStructExpression) {
			ArrayList<CFExpression> retval = new ArrayList<CFExpression>();
			for( Object element: ((CFStructExpression) expression).getElements()){
				retval.add((CFStructElementExpression)element);
			}
			return retval;
		} else if (expression instanceof CFStructElementExpression) {
			return wrap(((CFStructElementExpression) expression).getValue());
		} 
		return new ArrayList<CFExpression>();
	}
	
	protected List<CFExpression> wrap(CFExpression ... expression) {
		return Arrays.asList(expression);
	}
}
