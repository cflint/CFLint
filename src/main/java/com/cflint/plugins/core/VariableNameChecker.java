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
import cfml.parsing.cfscript.CFNewExpression;
import cfml.parsing.cfscript.CFVarDeclExpression;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class VariableNameChecker extends CFLintScannerAdapter {
    /**
     * Minimum number of characters for an variable name.
     */
    private int minVarLength = ValidName.MIN_VAR_LENGTH;

    /**
     * Maximum number of characters for an variable name.
     */
    private int maxVarLength = ValidName.MAX_VAR_LENGTH;

    /**
     * Minimum number of words in an variable name.
     */
    private int maxVarWords = ValidName.MAX_VAR_WORDS;
    
    private static final List<String> DEFAULT_EXCLUSIONS = Collections.singletonList("rc");
    private static final String PARAM_EXCLUSION_LIST = "ExclusionList";

    private final List<String> exclusions = new ArrayList<>();

    private void checkCFName(final Element element, final Context context, final BugList bugs, final int begLine, int offset, final String name) {
        if (element.getAttributeValue(name) != null) {
            final Attribute attribute = element.getAttributes().get(name);
            String varName;
            if (attribute != null) {
                varName = attribute.getValue();
                offset = attribute.getValueSegment().getBegin();
            } else {
                varName = "";
            }
            checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), begLine, offset, bugs,null);
        }
    }

    private void checkCFLoopName(final Element element, final Context context, final BugList bugs, final int begLine, int offset) {
        if (element.getAttributeValue(CF.INDEX) != null || element.getAttributeValue(CF.ITEM) != null) {
            String varName = "";
            final String index =  element.getAttributeValue(CF.INDEX);
            final String item =  element.getAttributeValue(CF.ITEM);

            if (index != null) {
                varName = index;
                offset = element.getAttributes().get(CF.INDEX).getValueSegment().getBegin();
            }
            else if (item != null) {
                varName = item;
                offset = element.getAttributes().get(CF.ITEM).getValueSegment().getBegin();
            }

            checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), begLine,
                    offset, bugs,null);
        }
    }
    
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        final String elementName = element.getName();
        final int begLine = element.getSource().getRow(element.getBegin());
        final int offset = element.getBegin();

        if (elementName.equals(CF.CFQUERY)) {
            checkCFName(element, context, bugs, begLine, offset, CF.NAME);
        } else if (elementName.equals(CF.CFINVOKE)) {
            checkCFName(element, context, bugs, begLine, offset, CF.RETURNVARIABLE);
        } else if (elementName.equals(CF.CFLOOP)) {
            checkCFLoopName(element, context, bugs, begLine, element.getBegin());
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
            checkExpression(expression, context, bugs);
        } else if (expression instanceof CFFullVarExpression) {
            checkFullExpression((CFFullVarExpression) expression, context, bugs);
        } else if (expression instanceof CFIdentifier && !(expression.getParent() instanceof CFNewExpression )) {
            checkIdentifier((CFIdentifier) expression, context, bugs);
        }
    }

    private void checkIdentifier(final CFIdentifier expression, final Context context, final BugList bugs) {
        final String varName = expression.getName();
        final int lineNo = expression.getLine() + context.startLine() - 1;
        final int offset = expression.getOffset() + context.offset();

        CFExpression parentExpression = (expression.getParent() instanceof CFExpression)?(CFExpression)expression.getParent():null;
        checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), lineNo, offset, bugs,parentExpression);
    }

    private void checkFullExpression(final CFFullVarExpression expression, final Context context, final BugList bugs) {
        final CFFullVarExpression cfFullVarExpression = expression;
        for (final CFExpression subexpression : cfFullVarExpression.getExpressions()) {
            if (subexpression instanceof CFIdentifier) {
                final String varName = ((CFIdentifier) subexpression).getName();
                final int lineNo = ((CFIdentifier) subexpression).getLine() + context.startLine() - 1;
                final int offset = ((CFIdentifier) subexpression).getOffset() + context.offset();

                // can be null for nested arrays if so ignore
                if (varName != null) {
                    checkNameForBugs(context, cfFullVarExpression.Decompile(0), varName,
                        context.getFilename(), context.getFunctionName(), lineNo, offset, bugs,expression);
                }
            }
        }
    }

    private void checkExpression(final CFExpression expression, final Context context, final BugList bugs) {
        final CFVarDeclExpression cfVarDeclExpression = (CFVarDeclExpression) expression;
        final int lineNo = expression.getLine() + context.startLine() - 1;
        final int offset = expression.getOffset() + context.offset() + 4; // 'var ' == 4 chars
        final String varName = cfVarDeclExpression.getName();
        checkNameForBugs(context, varName, varName, context.getFilename(), context.getFunctionName(), lineNo, offset, bugs,expression);
    }

    private void parseParameters(final ValidName name) throws ConfigError {
        if (getParameter("MinLength") != null) {
            try {
                minVarLength = Integer.parseInt(getParameter("MinLength"));
            } catch (final Exception e) {
                throw new ConfigError("Minimum length need to be an integer.");
            }
        }

        if (getParameter("MaxLength") != null) {
            try {
                maxVarLength = Integer.parseInt(getParameter("MaxLength"));
            } catch (final Exception e) {
                throw new ConfigError("Maximum length need to be an integer.");
            }
        }

        if (getParameter("MaxWords") != null) {
            try {
                maxVarWords = Integer.parseInt(getParameter("MaxWords"));
            } catch (final Exception e) {
                throw new ConfigError("Maximum no of words need to be an integer.");
            }
        }

        if (getParameter("FlagVariableNamePrefix") != null) {
            name.setPrefixesToAvoid(getParameter("FlagVariableNamePrefix").split(","));
        }
        if (getParameter("FlagVariableNameSuffix") != null) {
            name.setSuffixesToAvoid(getParameter("FlagVariableNameSuffix").split(","));
        }
        if (getParameter("RequiredVariableNamePrefix") != null) {
            name.setRequiredPrefixList(getParameter("RequiredVariableNamePrefix").split(","));
        }
    }

    public void checkNameForBugs(final Context context, final String fullVariable, final String variable,
            final String filename, final String functionName, final int line, final int offset, final BugList bugs,
            final CFExpression cfExpression) {
        if (excludeFromAnalyse(variable)) {
            return;
        }

        final CFScopes scope = new CFScopes();
        final String varScope = scope.getScope(fullVariable);
        final ValidName name = new ValidName(minVarLength, maxVarLength, maxVarWords);

        try {
            parseParameters(name);
        } catch (ConfigError configError) {
            // Carry on with defaults
        }

        Context parent = context.getParent(ContextType.FUNCTION);
        
        if (name.isInvalid(variable)) {
            parent.addUniqueMessage("VAR_INVALID_NAME", variable, this, line, offset, cfExpression);
        }
        if (!scope.isCFScoped(variable) && name.isUpperCase(variable) && !getParameterNotNull("IgnoreAllCapsInScopes").toLowerCase().contains(varScope)) {
            parent.addUniqueMessage("VAR_ALLCAPS_NAME", variable, this, line, offset, cfExpression);
        }
        if (scope.isCFScoped(variable) && name.isUpperCase(variable) && !getParameterNotNull("IgnoreUpperCaseScopes").contains(variable)) {
            parent.addUniqueMessage("SCOPE_ALLCAPS_NAME", variable, this, line, offset, cfExpression);
        }
        if (name.tooShort(variable)) {
            parent.addUniqueMessage("VAR_TOO_SHORT", variable, this, line, offset, cfExpression);
        }
        if (name.tooLong(variable)) {
            parent.addUniqueMessage("VAR_TOO_LONG", variable, this, line, offset, cfExpression);
        }
        if (!name.isUpperCase(variable) && name.tooWordy(variable)) {
            parent.addUniqueMessage("VAR_TOO_WORDY", variable, this, line, offset, cfExpression);
        }
        if (name.isTemporary(variable)) {
            parent.addUniqueMessage("VAR_IS_TEMPORARY", variable, this, line, offset, cfExpression);
        }
        if (name.hasPrefixOrPostfix(variable) && !getParameterNotNull("IgnorePrefixPostfixOn").contains(variable)) {
            parent.addUniqueMessage("VAR_HAS_PREFIX_OR_POSTFIX", variable, this, line, offset, cfExpression);
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
