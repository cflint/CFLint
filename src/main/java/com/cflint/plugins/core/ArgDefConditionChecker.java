package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public class ArgDefConditionChecker extends CFLintScannerAdapter {

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			final String code = function.Decompile(0);
			
			for (final CFFunctionParameter argument : function.getFormals()) {
				final String name = argument.getName();
				final boolean checked = isCheck(code,name); 
				if (!argument.toString().contains("required") && !argument.toString().contains("=") && !checked) {
					context.addMessage("ARG_DEFAULT_MISSING", name);
				}
			}
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfargument")) {
			final String name = element.getAttributeValue("name");
			final boolean required = CFTool.toBoolean(element.getAttributeValue("required"));
			final String defaultExpr = element.getAttributeValue("default");
			final String code = element.getParentElement().toString();
			final boolean checked = isCheck(code,name); 
			
			if (!required && defaultExpr == null && !checked) {
				element.getSource().getRow(element.getBegin());
				element.getSource().getColumn(element.getBegin());
				context.addMessage("ARG_DEFAULT_MISSING", name);
			}
		}
	}
	
	private boolean isCheck(String content, final String name){
		boolean checked = false;
		content = content.replace(" ", "").replace("'", "\"").toLowerCase();
		boolean structKeyCheck = (content.contains("structkeyexists(arguments,\""+name+"\""));
		boolean isDefinedCheck = (content.contains("isdefined(\"arguments."+name+"\""));
		checked = structKeyCheck || isDefinedCheck;
		return checked;
	}

}
