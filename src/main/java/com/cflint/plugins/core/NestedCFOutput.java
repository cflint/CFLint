package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

public class NestedCFOutput extends CFLintScannerAdapter {

	public static final String CFOUTPUT = "cfoutput";

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals(CFOUTPUT)) {
			final Element parent = CFTool.getNamedParent(element, CFOUTPUT);
			if(parent != null){
				if (parent.getAttributeValue("group") == null && anyContainingCFOutputHasQuery(parent)) {
					element.getSource().getRow(element.getBegin());
					element.getSource().getColumn(element.getBegin());
					context.addMessage("NESTED_CFOUTPUT", "");
				}
			}
		}
	}
	
	final boolean anyContainingCFOutputHasQuery(Element element){
		if(element == null){
			return false;
		}
		if(element.getAttributeValue("query") != null){
			return true;
		}
		return anyContainingCFOutputHasQuery(CFTool.getNamedParent(element, CFOUTPUT));
	}

}
