package com.cflint.plugins.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;

import com.cflint.BugList;
import com.cflint.CFLint;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.CFLintSet;
import com.cflint.plugins.Context;
import com.cflint.plugins.Context.ContextMessage;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFNewExpression;
import ro.fortsoft.pf4j.Extension;

@Extension
public class PackageCaseChecker extends CFLintScannerAdapter implements CFLintSet {

    private final Map<String, HashSet<String[]>> componentRegister = new HashMap<>();
    private final Map<String, List<PackageCaseCheckerEntry>> expressionCheckRegister = new HashMap<>();
    private CFLint cflintRef;

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFunctionExpression) {
            final CFFunctionExpression funcExpr = (CFFunctionExpression) expression;
            if (isCreateObject(funcExpr)) {
                final String componentPath = funcExpr.getArgs().get(1).Decompile(0).replace("'", "");
                final String componentName = componentPath.replaceAll("^.+[.]", "");
                checkComponentRegister(context, componentPath, componentName);
            }
        } else if (expression instanceof CFNewExpression) {
            final CFNewExpression newExpr = (CFNewExpression) expression;
            final String componentPath = newExpr.getComponentPath().Decompile(0);
            final List<CFExpression> exprs = newExpr.getComponentPath().decomposeExpression();
            final String componentName = exprs.isEmpty() ? "" : exprs.get(exprs.size() - 1).toString();
            checkComponentRegister(context, componentPath, componentName);
        }
    }

    private boolean checkComponentRegister(final Context context, final String componentPath, final String componentName) {
        return checkComponentRegister(context, componentPath, componentName, false);
    }

    private boolean checkComponentRegister(final Context context, final String componentPath, final String componentName, Boolean fromExpressionRegister) {
        String lowerComponentName = componentName.toLowerCase();
        String lowerComponentPath = componentPath.toLowerCase();
        if (componentRegister.containsKey(lowerComponentName)) {
            HashSet<String[]> filePathOfComponents = componentRegister.get(lowerComponentName);
            for (String[] filePathOfComponent : filePathOfComponents) {
                if (filePathOfComponent[1].endsWith(lowerComponentPath)) {
                    if (!filePathOfComponent[0].endsWith(componentPath)) {
                        final String expectedPath = filePathOfComponent[0].substring(filePathOfComponent[0].length() - componentPath.length());
                        context.addMessage("PACKAGE_CASE_MISMATCH", expectedPath);
                    }
                    return true;
                }
            }
        }
        if (!fromExpressionRegister) {
            //otherwise remember the component use for when component is first registered.
            final String key = lowerComponentName;
            if (!expressionCheckRegister.containsKey(key)) {
                expressionCheckRegister.put(key, new ArrayList<PackageCaseCheckerEntry>());
            }
            expressionCheckRegister.get(key).add(new PackageCaseCheckerEntry(context, componentPath, componentName));
        }
        return false;
    }

    @Override
    public void startComponent(final Context context, final BugList bugs) {
        final String key = context.getComponentName().toLowerCase();
        if (!componentRegister.containsKey(key)) {
            componentRegister.put(key, new HashSet<String[]>());
        }
        String component_name=normalize(context.getFilename());
        componentRegister.get(key).add(new String[]{component_name, component_name.toLowerCase()});

        //if an expression already referenced this component, check it here:
        boolean matched = false;
        if (expressionCheckRegister.containsKey(key)) {
            Map<String, Boolean> lookupCache = new HashMap<String, Boolean>();
            List<PackageCaseCheckerEntry> clonedList = new ArrayList<PackageCaseCheckerEntry>();
            clonedList.addAll(expressionCheckRegister.get(key));
            boolean checkComponentRegisterResult = false;
            for (final PackageCaseCheckerEntry expressionEntry : clonedList) {
                String cacheKey = expressionEntry.componentPath + "||" + expressionEntry.componentName;
                if (!lookupCache.containsKey(cacheKey)) {
                    checkComponentRegisterResult = checkComponentRegister(expressionEntry.context, expressionEntry.componentPath, expressionEntry.componentName, true);
                    lookupCache.put(cacheKey, checkComponentRegisterResult);
                } else {
                    checkComponentRegisterResult = lookupCache.get(cacheKey);
                }

                if (checkComponentRegisterResult) {
                    matched = true;
                    for (ContextMessage message : expressionEntry.context.getMessages()) {
                        cflintRef.reportRule(expressionEntry.context.getElement(), null, expressionEntry.context, this, message);
                    }
                    expressionEntry.context.getMessages().clear();
                    break;
                }
            }
        }
        //If component matched, remove the remembered expression.  (Efficiency)
        if (matched) {
            expressionCheckRegister.remove(key);
        }
    }

    private String normalize(final String filename) {
        return filename.replaceAll(".[cC][fF][cC]$", "").replace("\\", ".").replace("/", ".");
    }

    private boolean isCreateObject(final CFFunctionExpression funcExpr) {
        return "createobject".equalsIgnoreCase(funcExpr.getFunctionName()) && funcExpr.getArgs().size() > 1
            && "'component'".equalsIgnoreCase(funcExpr.getArgs().get(0).Decompile(0));
    }

    static final class PackageCaseCheckerEntry {

        private final Context context;
        private final String componentPath;
        private final String componentName;

        public PackageCaseCheckerEntry(Context context, String componentPath, final String componentName) {
            this.context = context;
            this.componentPath = componentPath;
            this.componentName = componentName;
        }
    }

    @Override
    public void setCFLint(final CFLint cflint) {
        this.cflintRef = cflint;
    }
}
