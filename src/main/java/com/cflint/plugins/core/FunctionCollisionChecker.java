package com.cflint.plugins.core;

import java.io.IOException;
import java.util.ArrayList;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

/**
 * Avoid using function X checker.
 */
@Extension
public class FunctionCollisionChecker extends CFLintScannerAdapter {

	ArrayList<String> functions = new ArrayList<String>();

	public FunctionCollisionChecker() {
		super();

		final ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode actualObj = mapper
					.readTree(FunctionCollisionChecker.class.getResourceAsStream("/cfdocs/functions.json"));
			JsonNode listObj = actualObj.get("related");
			for (int i = 0; i < listObj.size(); i++) {
				functions.add(listObj.get(i).asText());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Check an CF function declaration for use of reserved BIF names.
	 *
	 * @param expression expression to check.
	 * @param context    expression context.
	 * @param bugs       list of bugs.
	 */
	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement functionExpression = (CFFuncDeclStatement) expression;
			final String name = functionExpression.getName().getFullName();
			if (functions.contains(name)) {
				context.addMessage("FUNCTION_NAME_COLLISION", name, this, null, null, functionExpression.getName());
			}
		}
	}

	@Override
	public void element(Element element, Context context, BugList bugs) {
		final String elementName = element.getName();

		if (elementName.equals(CF.CFFUNCTION)) {
			final String name = element.getAttributeValue("name");
			if (functions.contains(name)) {
				context.addMessage("FUNCTION_NAME_COLLISION", name);
			}
		}
	}
}
