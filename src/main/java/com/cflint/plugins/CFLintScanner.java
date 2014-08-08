package com.cflint.plugins;

import net.htmlparser.jericho.Element;

import com.cflint.BugList;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import ro.fortsoft.pf4j.ExtensionPoint;

public interface CFLintScanner extends ExtensionPoint {

	public void expression(CFExpression expression, Context context, BugList bugs);

	public void element(Element element, Context context, BugList bugs);

	public void expression(CFScriptStatement expression, Context context, BugList bugs);

}
