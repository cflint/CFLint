package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class ComponentLengthChecker extends CFLintScannerAdapter {
    private static final int LENGTH_THRESHOLD = 500;
    int lastLine = 0;
    
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        try{
            lastLine = Math.max(lastLine, expression.getToken().getLine());
        }catch(Exception e){}
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if(element != null && element.getName().equalsIgnoreCase("cfcomponent")){
            try{
                if (element != null && element.getSource() != null) {
                    lastLine= element.getSource().getRow(element.getEnd());
                }
            }catch(Exception e){}
        }
    }

    @Override
    public void endComponent(Context context, BugList bugs) {
        super.endComponent(context, bugs);
        final String lengthThreshold = getParameter("length");
        int length = LENGTH_THRESHOLD;

        if (lengthThreshold != null) {
            length = Integer.parseInt(lengthThreshold);
        }
        if (lastLine > length) {
            context.addMessage("EXCESSIVE_COMPONENT_LENGTH", Integer.toString(lastLine), this, 1);
        }
    }
}
