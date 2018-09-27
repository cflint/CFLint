package com.cflint.plugins.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class UnusedArgumentChecker extends CFLintScannerAdapter {

    // Use linked hash map to preserve the order of the elements.
//    protected Map<String, Boolean> methodArguments = new LinkedHashMap<>();
//    protected Map<String, Integer> argumentLineNo = new HashMap<>();
//    protected Map<String, Integer> argumentOffset = new HashMap<>();
    protected Map<String, ArgInfo> currentArgs = new LinkedHashMap<>();

    static class ArgInfo{
        Boolean used = false;
        Integer argumentLineNo;
        Integer argumentOffset;
        String type;
        String casedName;
    }
    
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFARGUMENT)) {
            final String name = element.getAttributeValue(CF.NAME) != null
                ? element.getAttributeValue(CF.NAME) : "";
            ArgInfo argInfo = new ArgInfo();
            argInfo.casedName=name;
            argInfo.argumentLineNo=context.startLine();
            argInfo.argumentOffset=element.getAttributeValue(CF.NAME) != null 
                    ? element.getAttributes().get(CF.NAME).getValueSegment().getBegin() : element.getBegin();
            argInfo.type=element.getAttributeValue(CF.TYPE);
            currentArgs.put(name.toLowerCase(), argInfo);
            final String code = element.getParentElement().toString();
            if (isUsed(code, name.toLowerCase())) {
                argInfo.used=true;
            }
        }
    }

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            for (final CFFunctionParameter argument : function.getFormals()) {
                final String name = argument.getName().toLowerCase(); 
                // CF variable names are not case sensitive
                ArgInfo argInfo = new ArgInfo();
                argInfo.casedName=argument.getName();
                argInfo.argumentLineNo=function.getLine();
                argInfo.argumentOffset=context.offset() + argument.getOffset();
                argInfo.type=argument.getType();
                currentArgs.put(name, argInfo);
                if (isUsed(function.Decompile(0), name)) {
                    argInfo.used=true;
                }
            }
        }
    }

    protected void useIdentifier(final CFFullVarExpression fullVarExpression) {
        if (!fullVarExpression.getExpressions().isEmpty()) {
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
        if (currentArgs.get(name) != null) {
            currentArgs.get(name).used=true;
        }
    }
    
    protected void useIdentifier(final CFFunctionExpression identifier) {
        final String name = identifier.getName().toLowerCase();
        if (currentArgs.get(name) != null && CF.FUNCTION.equalsIgnoreCase(currentArgs.get(name).type)) {
            currentArgs.get(name).used=true;
        }
    }

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFullVarExpression) {
            useIdentifier((CFFullVarExpression) expression);
        } else if (expression instanceof CFIdentifier) {
            useIdentifier((CFIdentifier) expression);
        } else if(expression instanceof CFFunctionExpression){
            useIdentifier((CFFunctionExpression) expression);
        }
    }

    @Override
    public void startFunction(final Context context, final BugList bugs) {
        currentArgs.clear();
    }

    @Override
    public void endFunction(final Context context, final BugList bugs) {
        // sort by line number
        for (final Entry<String, ArgInfo> method : currentArgs.entrySet()) {
            final ArgInfo argInfo = method.getValue();
            final int offset = argInfo.argumentOffset;
            if (!argInfo.used) {
                context.addMessage("UNUSED_METHOD_ARGUMENT", argInfo.casedName, argInfo.argumentLineNo, offset);
            }
        }
    }

    private boolean isUsed(final String content, final String name) {
        boolean isUsed = false;
        String stripped = content.replace(" ", "").replace("'", "\"").toLowerCase();
        boolean structKeyCheck = (stripped.contains("arguments[\"" + name + "\"]"));
        boolean isDefinedCheck = (stripped.contains("arguments." + name));
        boolean isInCollection = (stripped.contains("argumentcollection=arguments"));
        isUsed = structKeyCheck || isDefinedCheck || isInCollection;
        return isUsed;
    }

}
