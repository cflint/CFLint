package com.cflint.plugins.core;

import com.cflint.Levels;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

// Deprecate?
@Extension
public class ScriptTagChecker extends CFLintScannerAdapter {
    private final String message = "Don't use inline <script> tags";
    private final Levels severity = Levels.ERROR;

    // rule: don't use inline javascript in cfm and cfc files
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals("script")) {
            final String src = element.getStartTag().toString();
            if (!src.matches(".*src=.*")) {
                context.addMessage("AVOID_USING_INLINE_JS", null);
            }
        }
    }
}
