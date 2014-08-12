package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Collection;
import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.cfFullVarExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;

@Extension
public class GlobalVarChecker implements CFLintScanner {

	final static Collection<String> variables = Arrays.asList("APPLICATION", "CGI", "COOKIE", "FORM", "REQUEST",
			"SERVER", "SESSION", "URL");

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		//if ((context.isInComponent() || context.isInFunction()) && expression instanceof CFIdentifier) {
		//	doIdentifier((CFIdentifier) expression,context,bugs);
		//}
		//else 
		if ((context.isInComponent() || context.isInFunction()) && expression instanceof cfFullVarExpression) {
			final CFExpression firstExpression = ((cfFullVarExpression) expression).getExpressions().get(0);
			if (firstExpression instanceof CFIdentifier) {
				doIdentifier((CFIdentifier) firstExpression,context,bugs);
			}
		}
	}
	
	protected void doIdentifier(final CFIdentifier expression, final Context context, final BugList bugs){
		final String name = expression.getName();
		if (variables.contains(name.toUpperCase()) && !context.getCallStack().isVariable(name)
				&& !context.getCallStack().isArgument(name)
				&& context.getCallStack().getPluginVar(GlobalVarChecker.class, name) == null) {
			context.getCallStack().setPluginVar(GlobalVarChecker.class, name, true);
			final Element elem = context.getElement();
			/*int line = 1;
			int column = 1;
			if(elem != null){
				line = elem.getSource().getRow(elem.getBegin());
				column = elem.getSource().getColumn(elem.getBegin());
			}else{
				line = expression.getLine();
				column = expression.getColumn();
			}*/
			bugs.add(new BugInfo.BugInfoBuilder()
					//.setLine(line)
					//.setColumn(column + expression.getColumn())
					.setMessageCode("GLOBAL_VAR")
					.setSeverity("WARNING")
					.setFilename(context.getFilename())
					.setFunction(context.getFunctionName())
					.setVariable(name)
					.setMessage(
							"Identifier " + name
									+ " is global, referencing in a CFC or function should be avoided.")
					.build(expression, context.getElement()));
		}
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {

	}

	public void element(final Element element, final Context context, final BugList bugs) {

	}

}
