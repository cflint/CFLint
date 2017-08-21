package com.cflint.plugins.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cflint.CF;
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

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String elementName = element.getName();
        final int begLine = element.getSource().getRow(element.getBegin());

        if (elementName.equals(CF.CFQUERY)) {
            if (element.getAttributeValue(CF.NAME) != null) {
                final String varName = element.getAttributeValue(CF.NAME) != null ? element.getAttributeValue(CF.NAME)
                        : "";
                checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), begLine,
                        bugs);
            }
        } else if (elementName.equals(CF.CFINVOKE)) {
            if (element.getAttributeValue(CF.RETURNVARIABLE) != null) {
                final String varName = element.getAttributeValue(CF.RETURNVARIABLE) != null
                        ? element.getAttributeValue(CF.RETURNVARIABLE) : "";
                checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), begLine,
                        bugs);
            }
        } else if (elementName.equals(CF.CFLOOP)) {
            if (element.getAttributeValue(CF.INDEX) != null || element.getAttributeValue(CF.ITEM) != null) {
                final String varName = element.getAttributeValue(CF.INDEX) != null ? element.getAttributeValue(CF.INDEX)
                        : (element.getAttributeValue(CF.ITEM) != null ? element.getAttributeValue(CF.ITEM) : "");
                checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), begLine,
                        bugs);
            }
        }
    }

    @Override
    public void setParameter(final String name, final Object value) {
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
                    
                    // can be null for nested arrays if so ignore
                    if (varName != null) {
                        checkNameForBugs(context, cfFullVarExpression.Decompile(0), varName,
                            context.getFilename(), context.getFunctionName(), lineNo, bugs);
                    }
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
        final String varScope = scope.getScope(fullVariable);
        final ValidName name = new ValidName(minVarLength, maxVarLength, maxVarWords);
        if (getParameter("FlagVariableNamePrefix") != null) {
            name.setPrefixesToAvoid(getParameter("FlagVariableNamePrefix").split(","));
        }
        if (getParameter("FlagVariableNameSuffix") != null) {
            name.setSuffixesToAvoid(getParameter("FlagVariableNameSuffix").split(","));
        }
        if (getParameter("RequiredVariableNamePrefix") != null) {
            name.setRequiredPrefixList(getParameter("RequiredVariableNamePrefix").split(","));
        }

        if (name.isInvalid(variable)) {
            context.getParent(ContextType.FUNCTION).addUniqueMessage("VAR_INVALID_NAME", variable, this, line);
        }
        if (!scope.isCFScoped(variable) && name.isUpperCase(variable)) {
            if (!getParameterNotNull("IgnoreAllCapsInScopes").toLowerCase().contains(varScope))
            context.getParent(ContextType.FUNCTION).addUniqueMessage("VAR_ALLCAPS_NAME", variable, this, line);
        }
        if (scope.isCFScoped(variable) && name.isUpperCase(variable) && !getParameterNotNull("IgnoreUpperCaseScopes").contains(variable)) {
            context.getParent(ContextType.FUNCTION).addUniqueMessage("SCOPE_ALLCAPS_NAME", variable, this, line);
        }
        if (name.tooShort(variable)) {
            context.getParent(ContextType.FUNCTION).addUniqueMessage("VAR_TOO_SHORT", variable, this, line);
        }
        if (name.tooLong(variable)) {
            context.getParent(ContextType.FUNCTION).addUniqueMessage("VAR_TOO_LONG", variable, this, line);
        }
        if (!name.isUpperCase(variable) && name.tooWordy(variable)) {
            context.getParent(ContextType.FUNCTION).addUniqueMessage("VAR_TOO_WORDY", variable, this, line);
        }
        if (name.isTemporary(variable)) {
            context.getParent(ContextType.FUNCTION).addUniqueMessage("VAR_IS_TEMPORARY", variable, this, line);
        }
        if (name.hasPrefixOrPostfix(variable)) {
            context.getParent(ContextType.FUNCTION).addUniqueMessage("VAR_HAS_PREFIX_OR_POSTFIX", variable, this, line);
        }
    }

    protected boolean excludeFromAnalyse(final String variable) {
        return exclusions.contains(normalize(variable));
    }

    protected String normalize(final String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        return name.trim().toLowerCase();
    }
}
