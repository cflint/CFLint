package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import net.htmlparser.jericho.Element;

public class VarScoper extends CFLintScannerAdapter {

	public static final String VARIABLE = "variable";
	public static final String RESULT = "result";

	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFIdentifier) {
			// No issue
			if (expression instanceof CFFullVarExpression
					&& ((CFFullVarExpression) expression).getExpressions().size() > 1) {
				return;
			}
			final String name = ((CFIdentifier) expression).getName();
			if (context.isInFunction() && context.isInAssignmentExpression()
					&& !context.getCallStack().checkVariable(name) && !isGlobal(name)) {
				context.addMessage("MISSING_VAR", name);
			} else if (expression instanceof CFFullVarExpression) {
				final CFFullVarExpression fullVarExpr = (CFFullVarExpression) expression;
				expression(fullVarExpr.getExpressions().get(0), context, bugs);
			}
		}
	}

	public VarScoper() {
		// CHECK_ELEMENT_ATTRIBUTES.put("cfloop",
		// Arrays.asList("index","item"));
		CHECK_ELEMENT_ATTRIBUTES.put("cfinvoke", Arrays.asList("returnvariable"));
		CHECK_ELEMENT_ATTRIBUTES.put("cffile", Arrays.asList(VARIABLE));
		CHECK_ELEMENT_ATTRIBUTES.put("cfsavecontent", Arrays.asList(VARIABLE));
		CHECK_ELEMENT_ATTRIBUTES.put("cfhttp", Arrays.asList(RESULT));
		CHECK_ELEMENT_ATTRIBUTES.put("cfquery", Arrays.asList(RESULT));
		CHECK_ELEMENT_ATTRIBUTES.put("cfmail", Arrays.asList("query"));
		CHECK_ELEMENT_ATTRIBUTES.put("cfftp", Arrays.asList(RESULT));
		CHECK_ELEMENT_ATTRIBUTES.put("cfwddx", Arrays.asList("output"));
		CHECK_ELEMENT_ATTRIBUTES.put("cfexecute", Arrays.asList(VARIABLE));
		CHECK_ELEMENT_ATTRIBUTES.put("cfntauthenticate", Arrays.asList(RESULT));
		CHECK_ELEMENT_ATTRIBUTES.put("cfxml", Arrays.asList(VARIABLE));

	}

	Map<String, List<String>> CHECK_ELEMENT_ATTRIBUTES = new HashMap<String, List<String>>();
	List<String> CHECK_NAMES = Arrays.asList(new String[] { "cfquery", "cfstoredproc", "cffeed", "cfdirectory",
			"cfform", "cfftp", "cfobject", "cfsearch", "cfprocresult", "cfpop", "cfregistry", "cfreport", "cfdbinfo",
			"cfdocument", "cfcollection", "cfpdf", "cfzip", "cfldap" });

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		final String name = element.getName();
		if (name != null && name.trim().length() > 0 && context.isInFunction()) {
			if (CHECK_NAMES.contains(name.toLowerCase())) {
				assertVariable(element, context, bugs, element.getAttributeValue("name"));
			}
			if (CHECK_ELEMENT_ATTRIBUTES.containsKey(name.toLowerCase())) {
				for (final String attrName : CHECK_ELEMENT_ATTRIBUTES.get(name.toLowerCase())) {
					assertVariable(element, context, bugs, element.getAttributeValue(attrName));
				}
			}
			if (name.equalsIgnoreCase("cfprocparam")) {
				final String typeVar = element.getAttributeValue("type");
				if (typeVar != null && (typeVar.equalsIgnoreCase("out") || typeVar.equalsIgnoreCase("inout"))) {
					assertVariable(element, context, bugs, element.getAttributeValue(VARIABLE));
				}
			}
			if (name.equalsIgnoreCase("cffeed")) {
				final String typeVar = element.getAttributeValue("action");
				if (typeVar != null && typeVar.equalsIgnoreCase("read")) {
					assertVariable(element, context, bugs, element.getAttributeValue("query"));
				}
			}
		}
	}

	protected void assertVariable(final Element element, final Context context, final BugList bugs,
			final String inameVar) {
		final String nameVar = inameVar == null ? null : inameVar.split("\\.")[0].split("\\[")[0];
		if (nameVar != null && !context.getCallStack().checkVariable(nameVar) && !isGlobal(nameVar)) {
			context.addMessage("MISSING_VAR", inameVar);
		}
	}

	final static Collection<String> variables = Arrays.asList("APPLICATION", "CGI", "COOKIE", "FORM", "REQUEST",
			"SERVER", "SESSION", "URL");

	private boolean isGlobal(final String nameVar) {
		return nameVar != null && variables.contains(nameVar.toUpperCase().trim());
	}

}
