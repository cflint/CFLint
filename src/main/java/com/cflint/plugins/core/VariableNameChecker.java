package com.cflint.plugins.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.plugins.Context.ContextType;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFVarDeclExpression;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class VariableNameChecker extends CFLintScannerAdapter {
    public static final String VARIABLE = "Variable ";

    private static final List<String> DEFAULT_EXCLUSIONS = Collections.singletonList("rc");
    private static final String PARAM_EXCLUSION_LIST = "ExclusionList";

    private final List<String> exclusions = new ArrayList<>();

    final String severity = "INFO";

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String elementName = element.getName();
        final int begLine = element.getSource().getRow(element.getBegin());

        if (elementName.equals("cfquery")) {
            if (element.getAttributeValue("name") != null) {
                final String varName = element.getAttributeValue("name") != null ? element.getAttributeValue("name")
                        : "";
                checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), begLine,
                        bugs);
            }
        } else if (elementName.equals("cfinvoke")) {
            if (element.getAttributeValue("returnvariable") != null) {
                final String varName = element.getAttributeValue("returnvariable") != null
                        ? element.getAttributeValue("returnvariable") : "";
                checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), begLine,
                        bugs);
            }
        } else if (elementName.equals("cfloop")) {
            if (element.getAttributeValue("index") != null || element.getAttributeValue("item") != null) {
                final String varName = element.getAttributeValue("index") != null ? element.getAttributeValue("index")
                        : (element.getAttributeValue("item") != null ? element.getAttributeValue("item") : "");
                checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), begLine,
                        bugs);
            }
        }
    }

    @Override
    public void setParameter(String name, String value) {
        super.setParameter(name, value);

        populateExclusions();
    }

    private void populateExclusions() {
        exclusions.clear();

        final String exclusionList = getParameter(PARAM_EXCLUSION_LIST);

        if (exclusionList == null) {
            exclusions.addAll(DEFAULT_EXCLUSIONS);
            return;
        }

        final String[] split = exclusionList.split(",");

        for (String name : split) {
            final String normalizedValue = normalize(name);
            if (normalizedValue != null) {
                exclusions.add(normalizedValue);
            }
        }
    }

    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if (expression instanceof CFVarDeclExpression) {
            final CFVarDeclExpression cfVarDeclExpression = (CFVarDeclExpression) expression;
            final int lineNo = expression.getLine() + context.startLine() - 1;
            final String varName = cfVarDeclExpression.getName();
            checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), lineNo, bugs);
        } else if (expression instanceof CFFullVarExpression) {
            final CFFullVarExpression cfFullVarExpression = (CFFullVarExpression) expression;
            for (final CFExpression subexpression : cfFullVarExpression.getExpressions()) {
                if (subexpression instanceof CFIdentifier) {
                    final String varName = ((CFIdentifier) subexpression).getName();
                    final int lineNo = ((CFIdentifier) subexpression).getLine() + context.startLine() - 1;

                    checkNameForBugs(context, cfFullVarExpression.Decompile(0), varName, context.getFilename(),
                            context.getFunctionName(), lineNo, bugs);
                }
            }
        } else if (expression instanceof CFIdentifier) {
            final String varName = ((CFIdentifier) expression).getName();
            final int lineNo = ((CFIdentifier) expression).getLine() + context.startLine() - 1;

            checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), lineNo, bugs);
        }
    }

    public void checkNameForBugs(final Context context, final String fullVariable, final String variable,
            final String filename, final String functionName, final int line, final BugList bugs) {
        if (excludeFromAnalyse(variable)) {
            return;
        }

        int minVarLength = ValidName.MIN_VAR_LENGTH;
        int maxVarLength = ValidName.MAX_VAR_LENGTH;
        int maxVarWords = ValidName.MAX_VAR_WORDS;

        if (getParameter("MinLength") != null) {
            try {
                minVarLength = Integer.parseInt(getParameter("MinLength"));
            } catch (final Exception e) {
            }
        }

        if (getParameter("MaxLength") != null) {
            try {
                maxVarLength = Integer.parseInt(getParameter("MaxLength"));
            } catch (final Exception e) {
            }
        }

        if (getParameter("MaxWords") != null) {
            try {
                maxVarWords = Integer.parseInt(getParameter("MaxWords"));
            } catch (final Exception e) {
            }
        }

        final CFScopes scope = new CFScopes();
        final ValidName name = new ValidName(minVarLength, maxVarLength, maxVarWords);

        if (name.isInvalid(variable)) {
            context.getParent(ContextType.Function).addUniqueMessage("VAR_INVALID_NAME", variable, this, line);
        }
        if (!scope.isCFScoped(variable) && name.isUpperCase(variable)) {
            context.getParent(ContextType.Function).addUniqueMessage("VAR_ALLCAPS_NAME", variable, this, line);
        }
        if (scope.isCFScoped(variable) && name.isUpperCase(variable) && (getParameter("IgnoreUpperCaseScopes") == null
                || !getParameter("IgnoreUpperCaseScopes").contains(variable))) {
            context.getParent(ContextType.Function).addUniqueMessage("SCOPE_ALLCAPS_NAME", variable, this, line);
        }
        if (name.tooShort(variable)) {
            context.getParent(ContextType.Function).addUniqueMessage("VAR_TOO_SHORT", variable, this, line);
        }
        if (name.tooLong(variable)) {
            context.getParent(ContextType.Function).addUniqueMessage("VAR_TOO_LONG", variable, this, line);
        }
        if (!name.isUpperCase(variable) && name.tooWordy(variable)) {
            context.getParent(ContextType.Function).addUniqueMessage("VAR_TOO_WORDY", variable, this, line);
        }
        if (name.isTemporary(variable)) {
            context.getParent(ContextType.Function).addUniqueMessage("VAR_IS_TEMPORARY", variable, this, line);
        }
        if (name.hasPrefixOrPostfix(variable)) {
            context.getParent(ContextType.Function).addUniqueMessage("VAR_HAS_PREFIX_OR_POSTFIX", variable, this, line);
        }
    }

    protected boolean excludeFromAnalyse(String variable) {
        return exclusions.contains(normalize(variable));
    }

    protected String normalize(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        return name.trim().toLowerCase();
    }
}