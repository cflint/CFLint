package com.cflint.plugins;

import java.util.HashMap;
import java.util.Map;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugList;

/**
 * Lint Rule Plugins can extend this adapter instead of implementing all the methods of CFLintScanner
 * 
 * @author eberlyrh
 *
 */
public class CFLintScannerAdapter implements CFLintScanner {

	Map<String, String> params = new HashMap<String,String>();
	public CFLintScannerAdapter() {
		super();
	}

	public void expression(CFExpression expression, Context context, BugList bugs){}

	public void element(Element element, Context context, BugList bugs){}

	public void expression(CFScriptStatement expression, Context context, BugList bugs){}
	
	public void setParameter(String name, String value){
		if(name != null){
			params.put(name.toLowerCase(), value);
			params.put(name, value);
		}
	}

	public String getParameter(String name){
		if(name != null){
			return params.get(name.toLowerCase());
		}
		return null;
	}

	public int currentLine(final CFExpression expression, final Context context) {
		return expression.getLine() + context.startLine() - 1;
	}

	public Map<String, String> getParams() {
		return params;
	}
}
