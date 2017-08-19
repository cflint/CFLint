package com.cflint.plugins.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cflint.BugList;
import com.cflint.CF;
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

    // Use linked hash map to preserve the order of the elements.
    protected Map<String, Boolean> methodArguments = new LinkedHashMap<String, Boolean>();
    protected Map<String, Integer> argumentLineNo = new HashMap<String, Integer>();

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
    	if (element.getName().equals(CF.CFARGUMENT)) {
            final String name = element.getAttributeValue(CF.NAME) != null
                    ? element.getAttributeValue(CF.NAME).toLowerCase() : "";
            methodArguments.put(name, false);
            setArgumentLineNo(name, context.startLine());
            final String code = element.getParentElement().toString();
            if(isUsed(code, name)){
            	methodArguments.put(name, true);
            }
        }
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
                if(isUsed(function.Decompile(0), name)){
                    methodArguments.put(name, true);
                }
            }
        }
    }

    protected void setArgumentLineNo(final String argument, final Integer lineNo) {
        if (argumentLineNo.get(argument) == null) {
            argumentLineNo.put(argument, lineNo);
        }
    }

    protected void useIdentifier(final CFFullVarExpression fullVarExpression) {
    	if (fullVarExpression.getExpressions().size() > 0) {
            final CFExpression identifier1 = fullVarExpression.getExpressions().get(0);
            if (identifier1 instanceof CFIdentifier) {
            	if ("arguments".equalsIgnoreCase(((CFIdentifier) identifier1).getName())
                        && fullVarExpression.getExpressions().size() > 1) {
                    final CFExpression identifier2 = fullVarExpression.getExpressions().get(1);
                    if (identifier2 instanceof CFIdentifier) {
                        useIdentifier((CFIdentifier) identifier2);
                    }
                } else {
                    useIdentifier((CFIdentifier) identifier1);
                }
            }
        }
    }

    protected void useIdentifier(final CFIdentifier identifier) {
    	final String name = identifier.getName().toLowerCase();
        if (methodArguments.get(name) != null) {
            methodArguments.put(name, true);
        }
    }

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
    	if (expression instanceof CFFullVarExpression) {
            useIdentifier((CFFullVarExpression) expression);
        } else if (expression instanceof CFIdentifier) {
            useIdentifier((CFIdentifier) expression);
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
        	final String name = method.getKey();
            final Boolean used = method.getValue();
            if (!used) {
                context.addMessage("UNUSED_METHOD_ARGUMENT", name, argumentLineNo.get(name));
            }
        }
    }
    
    private boolean isUsed(String content, final String name) {
    	boolean isUsed = false;
        content = content.replace(" ", "").replace("'", "\"").toLowerCase();
        boolean structKeyCheck = (content.contains("arguments[\"" + name + "\"]"));
        boolean isDefinedCheck = (content.contains("arguments." + name));
        boolean isInCollection = (content.replace(" ", "").contains("argumentcollection=arguments"));
        isUsed = structKeyCheck || isDefinedCheck || isInCollection;
        return isUsed;
    }

}
