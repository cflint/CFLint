package com.cflint.plugins;

import com.cflint.BugList;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

public interface CFLintScanner {

    void expression(CFExpression expression, Context context, BugList bugs);

    void element(Element element, Context context, BugList bugs);

    void expression(CFScriptStatement expression, Context context, BugList bugs);

    void setParameter(String name, Object value);

    void startFile(String fileName, BugList bugs);

    void endFile(String fileName, BugList bugs);

    void startComponent(Context context, BugList bugs);

    void endComponent(Context context, BugList bugs);

    void startFunction(Context context, BugList bugs);

    void endFunction(Context context, BugList bugs);

}
