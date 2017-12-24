package com.cflint.plugins.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final Map<String, List<String>> componentRegister = new HashMap<>();
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
        if (componentRegister.containsKey(componentName.toLowerCase())) {
            List<String> filePathOfComponents = componentRegister.get(componentName.toLowerCase());
            for (String filePathOfComponent : filePathOfComponents) {
                if (filePathOfComponent.toLowerCase().endsWith(componentPath.toLowerCase())) {
                    if (!filePathOfComponent.endsWith(componentPath)) {
                        final String expectedPath = filePathOfComponent.substring(filePathOfComponent.length() - componentPath.length());
                        context.addMessage("PACKAGE_CASE_MISMATCH", expectedPath);
                    }
                    return true;
                }
            }
        }
        //otherwise remember the component use for when component is first registered.
        final String key = componentName.toLowerCase();
        if (!expressionCheckRegister.containsKey(key)) {
            expressionCheckRegister.put(key, new ArrayList<PackageCaseCheckerEntry>());
        }
        expressionCheckRegister.get(key).add(new PackageCaseCheckerEntry(context, componentPath, componentName));
        return false;
    }

    @Override
    public void startComponent(final Context context, final BugList bugs) {
        final String key = context.getComponentName().toLowerCase();
        if (!componentRegister.containsKey(key)) {
            componentRegister.put(key, new ArrayList<String>());
        }
        componentRegister.get(key).add(normalize(context.getFilename()));

        //if an expression already referenced this component, check it here:
        boolean matched = false;
        if (expressionCheckRegister.containsKey(key)) {
            List<PackageCaseCheckerEntry> clonedList = new ArrayList<>();
            clonedList.addAll(expressionCheckRegister.get(key));
            for (final PackageCaseCheckerEntry expressionEntry : clonedList) {
                if (checkComponentRegister(expressionEntry.context, expressionEntry.componentPath, expressionEntry.componentName)) {
                    matched = true;
                    for (ContextMessage message : expressionEntry.context.getMessages()) {
                        cflintRef.reportRule(expressionEntry.context.getElement(), null, expressionEntry.context, this, message);
                    }
                    expressionEntry.context.getMessages().clear();
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
