package com.cflint.plugins.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFJavaMethodExpression;
import cfml.parsing.cfscript.cfFullVarExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;

public class QueryParamChecker implements CFLintScanner {

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof cfFullVarExpression) {
			final cfFullVarExpression cfFunctionExpr = (cfFullVarExpression) expression;
			StringBuilder varName = new StringBuilder();
			for(final CFExpression expression2: cfFunctionExpr.getExpressions()){
				if(expression2 instanceof CFJavaMethodExpression){
					final CFJavaMethodExpression javaexpression = (CFJavaMethodExpression) expression2;
					if(javaexpression.getFunctionName().equalsIgnoreCase("setSql") && javaexpression.getArguments().size()>0){
						CFExpression argsExpression = javaexpression.getArguments().get(0);
						if(argsExpression instanceof CFBinaryExpression){
							bugs.add(new BugInfo.BugInfoBuilder().setMessageCode("QUERYPARAM_REQ")
							.setMessage("setSql() statement should use .addParam() instead of #'s for security.")
							.setVariable(varName.toString())
							.setFunction(context.getFunctionName()).setSeverity("WARNING")
							.setFilename(context.getFilename()).setExpression(argsExpression.Decompile(0))
							.build(expression, context.getElement()));
							
						}
					}
				}else{ 
					if(varName.length()>0){
						varName.append(".");
					}
					varName.append(expression2.Decompile(0));
				}
			}
		}
	}

	public void element(final Element element, final Context context, final BugList bugs) {
		if (// element.getName().equals("cfcomponent") ||
		element.getName().equals("cfquery")) {
			final String content = element.getTextExtractor().toString();
			if(content.indexOf("#") > 0){
				final Pattern pattern = Pattern.compile("#(.+?)#");
				final Matcher matcher = pattern.matcher(content);
				while(matcher.find()){
					if(matcher.groupCount()>=1){
						final String variableName = matcher.group(1);
						final int line = element.getSource().getRow(element.getBegin());
						final int column = element.getSource().getColumn(element.getBegin());
						bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setColumn(column).setMessageCode("QUERYPARAM_REQ")
								.setSeverity("INFO").setFilename(context.getFilename()).setFunction(context.getFunctionName())
								.setVariable(variableName)
								.setMessage(element.getName() + " should use <cfqueryparam/> for security reasons.").build());
					}
				}
			}
//			final String outputAttr = element.getAttributeValue("output");
//			if (outputAttr == null) {
//				final int line = element.getSource().getRow(element.getBegin());
//				final int column = element.getSource().getColumn(element.getBegin());
//				bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setColumn(column).setMessageCode("OUTPUT_ATTR")
//						.setSeverity("INFO").setFilename(context.getFilename()).setFunction(context.getFunctionName())
//						.setMessage(element.getName() + " should have @output='false' ").build());
//			}
		}
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}

}
