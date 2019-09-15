package com.cflint.plugins.core;

import java.util.HashSet;
import java.util.Set;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFVarDeclExpression;

public class ArgVarChecker extends CFLintScannerAdapter {

    /**
     * Report each occurrence once per file/function
     */
    private Set<String> alreadyReportedExpression = new HashSet<>();
    private Set<String> alreadyReportedFullExpression = new HashSet<>();

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFVarDeclExpression) {
            final String name = ((CFVarDeclExpression) expression).getName();
            if (context.isInFunction() && context.getCallStack().hasArgument(name)) {
                checkExpression(context, name);
            }
        } else if (expression instanceof CFFullVarExpression) {
            final CFFullVarExpression fullVarExpr = (CFFullVarExpression) expression;
            if (checkFullExpression(context, fullVarExpr)) {
                return;
            }
            expression(fullVarExpr.getExpressions().get(0), context, bugs);
        } else if(expression instanceof CFIdentifier && !context.isInStructKeyExpression()) {
        	CFIdentifier cfi = (CFIdentifier)expression;
        	if(context.getCallStack().isArgument(cfi.getName())) {
                final String fileKey = context.fileFunctionString();
                if (alreadyReportedFullExpression.contains(fileKey)) {
                    return;
                } else {
                    alreadyReportedFullExpression.add(fileKey);
                }
                context.addMessage("ARG_VAR_MIXED", cfi.getName(),this,null,null,cfi);            	
            }
        }
    }

    private boolean checkFullExpression(final Context context, final CFFullVarExpression fullVarExpr) {
        if (fullVarExpr.getExpressions().get(0) instanceof CFIdentifier) {
            final CFIdentifier cfIdentifier1 = (CFIdentifier) fullVarExpr.getExpressions().get(0);
            final CFIdentifier cfIdentifier2 = fullVarExpr.getExpressions().size()>1&& fullVarExpr.getExpressions().get(1) instanceof CFIdentifier?(CFIdentifier) fullVarExpr.getExpressions().get(1):null;
            final String fileKey = context.fileFunctionString();
            if ("arguments".equalsIgnoreCase(cfIdentifier1.getName())
                   ) {
                final String name = cfIdentifier2!=null ?cfIdentifier2.getName():null;
                if (name!=null && context.getCallStack().isVariable(name)) {
                    if (alreadyReportedFullExpression.contains(fileKey)) {
                        return true;
                    } else {
                        alreadyReportedFullExpression.add(fileKey);
                    }
                    context.addMessage("ARG_VAR_MIXED", name,this,null,null,cfIdentifier2);
                }
            }
            else if("VARIABLES".equalsIgnoreCase(cfIdentifier1.getName())||"local".equalsIgnoreCase(cfIdentifier1.getName())) {
            	if(cfIdentifier2!=null && context.getCallStack().isArgument(cfIdentifier2.getName())) {
                    if (alreadyReportedFullExpression.contains(fileKey)) {
                        return true;
                    } else {
                        alreadyReportedFullExpression.add(fileKey);
                    }
                    context.addMessage("ARG_VAR_MIXED", cfIdentifier2.getName(),this,null,null,cfIdentifier2);
                }	
            }else if(context.getCallStack().isArgument(cfIdentifier1.getName())) {
                if (alreadyReportedFullExpression.contains(fileKey)) {
                    return true;
                } else {
                    alreadyReportedFullExpression.add(fileKey);
                }
                context.addMessage("ARG_VAR_MIXED", cfIdentifier1.getName(),this,null,null,cfIdentifier1);            	
            }
        }
        return false;
    }

    private void checkExpression(final Context context, final String name) {
        final String fileKey = context.fileFunctionString();
        if (alreadyReportedExpression.contains(fileKey)) {
            return;
        } else {
            alreadyReportedExpression.add(fileKey);
        }
        context.addMessage("ARG_VAR_CONFLICT", name);
    }

}
