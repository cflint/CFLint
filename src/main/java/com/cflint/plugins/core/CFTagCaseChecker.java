package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class CFTagCaseChecker extends CFLintScannerAdapter {
    private static final String CFTAG_PREFERRED_CASE = "CFTAG_PREFERRED_CASE";

    // rule: tag should be lowercase or camelCase
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        boolean encourageUpper = true;
        if (context.getConfiguration().getParameter(this,"PreferCase") != null) {
            try {
                encourageUpper = "upper".equalsIgnoreCase(context.getConfiguration().getParameter(this,"PreferCase"));
            } catch (final Exception e) {
            }
        }
        final String tag = element.getStartTag().toString();
        if ("cf".equalsIgnoreCase(tag.substring(1, 3))) {
            int index = tag.indexOf(' ');
            if (index == -1) {
                index = tag.indexOf('>');
            }
            final String cfTag = tag.substring(1, index);
            final String nonPreferredCase = encourageUpper ? cfTag.toLowerCase() : cfTag.toUpperCase();
            /*
             * ensuring the tag is not pure uppercase instead of checking to see
             * if the tag is lowercase or camelcase as some camelcase can get
             * hairy e.g. <cfmcGrabURLFromHTTPSite>
             */
            if (cfTag.equals(nonPreferredCase)) {
                final int begLine = element.getSource().getRow(element.getBegin());
                context.addMessage(CFTAG_PREFERRED_CASE, cfTag, this, begLine, element.getBegin());
            }
        }
    }
}
