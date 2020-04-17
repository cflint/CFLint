package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import ro.fortsoft.pf4j.Extension;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;

@Extension
public class ComponentUseChecker extends CFLintScannerAdapter {

    public static enum UsageTypes {
        NEW_COMPONENT,
        CREATEOBJECT,
        COMPONENT_EXTENDS,
        COMPONENT_IMPLEMENTS,
        CFML_TEMPLATE,
        PROP_INJECT,
        UNDEFINED
    }

    ;
    static final HashMap<UsageTypes, Pattern> patternsMap;
    static final Pattern PascalCasePattern;

    static {
        patternsMap = new HashMap<UsageTypes, Pattern>();
        patternsMap.put(UsageTypes.CFML_TEMPLATE, Pattern.compile("template=\"(.*)\""));
        patternsMap.put(UsageTypes.CREATEOBJECT, Pattern.compile("createObject\\([\"\\']component[\"\\']\\,[\"\\']([^\\s]+)[\"\\']"));
        patternsMap.put(UsageTypes.COMPONENT_EXTENDS, Pattern.compile("\\s+extends\\s?=\\s?[\\'\"]([^\\s]+)[\'\"]"));
        patternsMap.put(UsageTypes.COMPONENT_IMPLEMENTS, Pattern.compile("\\s+implements\\s?=\\s?[\\'\"]([^\\s]+)[\\'\"]"));
        //patternsMap.put(UsageTypes.PROP_INJECT,Pattern.compile("template=\"(.*)\""));
        patternsMap.put(UsageTypes.NEW_COMPONENT, Pattern.compile("new\\D([a-zA-Z0-9\\.]*)\\(.*"));
        PascalCasePattern = Pattern.compile("^[A-Z][a-z]+(?:[A-Z][a-z]+)*$");
    }

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFExpressionStatement) {
            final String code = ((CFExpressionStatement) expression).getExpression().Decompile(0);
            final int lineNo = ((CFExpressionStatement) expression).getLine() + context.startLine() - 1;
            final int offset = ((CFExpressionStatement) expression).getOffset() + context.offset();
            UsageTypes componentUsage = getComponentUseIfAny(code);
            if (patternsMap.containsKey(componentUsage)) {
                Matcher matcher = patternsMap.get(componentUsage).matcher(code);
                String componentName = matcher.group(1);
                if (componentName.contains(".")) {
                    String[] paths = componentName.split(".");
                    componentName = paths[paths.length - 1];
                }
                if (!PascalCasePattern.matcher(componentName).matches()) {
                    context.addMessage("AVOID_USING_COMPONENT_NAME", null, lineNo, offset);
                }
            }
        }
    }

    protected UsageTypes getComponentUseIfAny(final String code) {
        String codeLine = code.replaceAll("\\s+", "").toLowerCase();
        if (codeLine.contains("createobject")) {
            return UsageTypes.CREATEOBJECT;
        } else if (codeLine.contains("new")) {
            return UsageTypes.NEW_COMPONENT;
        } else if (codeLine.contains("extends")) {
            return UsageTypes.COMPONENT_EXTENDS;
        } else if (codeLine.contains("implements")) {
            return UsageTypes.COMPONENT_EXTENDS;
        } else if (codeLine.contains("implements")) {
            return UsageTypes.PROP_INJECT;
        } else if (codeLine.contains("template")) {
            return UsageTypes.CFML_TEMPLATE;
        } else {
            return UsageTypes.UNDEFINED;
        }
    }

}
