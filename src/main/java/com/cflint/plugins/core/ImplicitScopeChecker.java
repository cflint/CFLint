package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFMember;
import cfml.parsing.cfscript.CFVarDeclExpression;
import net.htmlparser.jericho.Element;

public class ImplicitScopeChecker extends CFLintScannerAdapter {

	/**
	 * Report each occurrence once per file/function
	 */
	// private Set<String> alreadyReportedExpression = new HashSet<>();
	// private Set<String> unqualifiedExpression = new HashSet<>();
	// private Set<String> alreadyReportedFullExpression = new HashSet<>();

    // LinkedHashMap is ordered.
    protected Set<String> implicitScopedVariables = new HashSet<>();
    protected Set<String> scopedVariables = new HashSet<>();
    protected Set<String> variableScopedVariables = new HashSet<>();
    
    protected Map<String, VariableInfo> unscopedAssignedVariables = new LinkedHashMap<>();
    protected Map<String, VariableInfo> implicitIdentifierVariables = new LinkedHashMap<>();
    protected Boolean isCFM = false;

    private final Collection<String> scopes = Arrays.asList(CF.APPLICATION, CF.ATTRIBUTES, CF.REQUEST, CF.SERVER, CF.SESSION, CF.CFTHREAD, CF.FORM, CF.URL, CF.CGI, CF.COOKIE, CF.CLIENT, CF.FILE, CF.CFCATCH);
    private final Collection<String> implicitscopes = Arrays.asList(CF.FORM, CF.URL, CF.CGI, CF.COOKIE, CF.CLIENT, CF.FILE, CF.CFCATCH);
    private final Collection<String> variablescopes = Arrays.asList(CF.VARIABLES);

	
    /** 
     * @param expression expression
     * @param context context
     * @param bugs bugs
     */
    @Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        if(expression instanceof CFFullVarExpression) {
            checkFullExpression((CFFullVarExpression) expression, context, bugs);
        } else if (expression instanceof CFVarDeclExpression) {
            checkExpression((CFVarDeclExpression)expression, context);
        } else if (expression instanceof CFIdentifier) {
            final String name = ((CFIdentifier) expression).getName();
            if ( name != null ) {
                final boolean checkIsScope = isScope(name);
                final boolean checkIsInFunction = context.isInFunction();
                final boolean checkIsCallStackVariable = checkIsInFunction == true ? context.getCallStack().checkVariable(name) : false;

                if ( !checkIsScope && ( !checkIsInFunction || !checkIsCallStackVariable ) ) {
                    if (context.isInAssignmentExpression() && !(expression.getParent() instanceof CFMember) ) {
                        if ( !context.isInStructKeyExpression() ) {
                            unscopedAssignedVariables.put(name.toLowerCase(), new VariableInfo(name, expression, context));
                            //if ( context.isInAssignmentExpression() ) {
                                variableScopedVariables.add(name.toLowerCase());
                            //}
                        }
                    } else {
                        if ( !context.isInStructKeyExpression() ) {
                            implicitIdentifierVariables.put(name.toLowerCase(), new VariableInfo(name, expression, context));
                        }
                    }
                }
            }
        }
	}

	private void checkFullExpression(final CFFullVarExpression expression, final Context context, final BugList bugs) {
        final CFExpression variable = expression.getExpressions().get(0);
        final CFExpression variable2 = expression.getExpressions().size() > 1 ? expression.getExpressions().get(1) : null;
        if (variable instanceof CFIdentifier) {
            final CFIdentifier cfIdentifier1 = (CFIdentifier) variable;
			final CFIdentifier cfIdentifier2 = variable2 != null && variable2 instanceof CFIdentifier
							? (CFIdentifier) variable2
							    /* : ( variable2 instanceof CFMember 
                                    ? (((CFMember) variable2).getExpression() instanceof CFIdentifier 
                                        ? (CFIdentifier) ((CFMember) variable2).getExpression() 
                                        : null ) */
                                    : null;
            
            final String name1 = ((CFIdentifier) cfIdentifier1).getName();
            if ( isImplicitScope(name1) && cfIdentifier2 != null ) {
                final String name = ((CFIdentifier) cfIdentifier2).getName();
                if ( name != null ) {
                    implicitScopedVariables.add(name.toLowerCase());
                }
            } else if ( isVariableScope(name1) && cfIdentifier2 != null  ) {
                final String name = ((CFIdentifier) cfIdentifier2).getName();
                if ( name != null ) {
                    variableScopedVariables.add(name.toLowerCase());
                }
            } else if ( isScope(name1) && cfIdentifier2 != null  ) {
                final String name = ((CFIdentifier) cfIdentifier2).getName();
                if ( name != null ) {
                    scopedVariables.add(name.toLowerCase());
                }
            } else if ( expression.getExpressions().size() == 1 ) {
                if ( name1 != null ) {
                    final boolean checkIsInFunction = context.isInFunction();
                    final boolean checkIsCallStackVariable = checkIsInFunction == true ? context.getCallStack().checkVariable(name1) : false;
                    if ( !checkIsInFunction || !checkIsCallStackVariable ) {
                        unscopedAssignedVariables.put(name1.toLowerCase(), new VariableInfo(name1,cfIdentifier1,context));
                        if ( context.isInAssignmentExpression() ) {
                            variableScopedVariables.add(name1.toLowerCase());
                        }
                    }
                }
            }
        }

        for (CFExpression subexpr : expression.getExpressions()) {
            if (subexpr instanceof CFMember) {
                CFMember memberExpr = (CFMember) subexpr;
                if (memberExpr.getExpression() != null) {
                    expression(memberExpr.getExpression(), context, bugs);
                }
            }
        }

	}

	private void checkExpression(final CFVarDeclExpression expression, final Context context) {
        // final String name = expression.getName();
        // implicitIdentifierVariables.put(name.toLowerCase(), new VariableInfo(name, expression, context));
	}

    @Override
	public void startFile(String filename, BugList bugs) {
        final String ext = filename.substring(filename.length() - 3, filename.length());
        isCFM = "cfm".equals(ext);
        clearVariables(false);
	}

    @Override
	public void beforeEndFile(String filename, Context context, BugList bugs) {
        checkImplicitScopes(false, context);
	}

	@Override
	public void startFunction(Context context, BugList bugs) {
        clearVariables(true);
	}

    @Override
    public void endFunction(final Context context, final BugList bugs) {
        checkImplicitScopes(true, context);
    }
    
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
    }

    private void clearVariables(final Boolean isFunction) {
        if ( (isFunction && isCFM) || (!isFunction && !isCFM) ) {
            return;
        }
        implicitScopedVariables.clear();
        implicitIdentifierVariables.clear();
        unscopedAssignedVariables.clear();
        scopedVariables.clear();
    }
    
    private void checkImplicitScopes(final Boolean isFunction, final Context context) {
        if ( (isFunction && isCFM) || (!isFunction && !isCFM) ) {
            return;
        }
        // sort by line number
        for (final VariableInfo variable : implicitIdentifierVariables.values()) {
            // Doesn't exist as unscoped or VARIABLES, or is known as an implicit scope
            if ( ( unscopedAssignedVariables.get(variable.name.toLowerCase()) == null
            && variableScopedVariables.contains(variable.name.toLowerCase()) != true )
            || implicitScopedVariables.contains(variable.name.toLowerCase()) == true ) {
                context.addMessage(
                    "IMPLICIT_SCOPE",
                    variable.name,
                    this,
                    variable.lineNumber,
                    variable.offset,
                    variable.expression,
                    variable.context);
            }
        }
    }

    private boolean isImplicitScope(final String nameVar) {
        return nameVar != null && implicitscopes.contains(nameVar.toLowerCase().trim());
    }

    private boolean isScope(final String nameVar) {
        return nameVar != null && scopes.contains(nameVar.toLowerCase().trim());
    }

    private boolean isVariableScope(final String nameVar) {
        return nameVar != null && variablescopes.contains(nameVar.toLowerCase().trim());
    }

    public static class VariableInfo {
        private Integer lineNumber;
        private Integer offset;
        private String name;
        CFExpression expression;
        final Context context;

        public VariableInfo(final String name, final CFExpression expression,final Context context) {
            super();
            this.name = name;
            this.expression = expression;
            this.context=context;
        }
    }
}
