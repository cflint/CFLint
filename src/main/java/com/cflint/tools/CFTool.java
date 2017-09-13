package com.cflint.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cflint.Levels;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;

import cfml.parsing.cfscript.CFExpression;
import net.htmlparser.jericho.Element;

public class CFTool {

    private CFTool() {
        throw new IllegalStateException("CFTool utility class");
    }

    public static Element getNamedParent(final Element elem, final String tagName) {
    	Element parentElem = elem.getParentElement();
        while (parentElem != null && !parentElem.getName().equals(tagName)) {
        	parentElem = parentElem.getParentElement();
        }
        return parentElem;
    }

    public static boolean toBoolean(final String value) {
        if (value == null) {
            return false;
        }
        return "yes".equalsIgnoreCase(value.trim()) || "true".equalsIgnoreCase(value.trim());
    }

    public static Element getElementBefore(final Element element, final List<Element> elements) {
        if (element != null && elements != null && elements.indexOf(element) > 0) {
            return elements.get(elements.indexOf(element) - 1);
        }
        return null;
    }

    public static Map<String, CFExpression> convertMap(final Map<? extends CFExpression, CFExpression> map) {
        final Map<String, CFExpression> retval = new HashMap<>();
        for (Entry<? extends CFExpression, CFExpression> entry : map.entrySet()) {
            retval.put(entry.getKey().Decompile(0).toLowerCase(), entry.getValue());
        }
        return retval;
    }

    /*
     * Apply the configuration to the existing rule. Overlay it.
     */
    public static void merge(final PluginMessage cfgMsg, final PluginMessage msg) {
        if (!isEmpty(cfgMsg.getMessageText())) {
            msg.setMessageText(cfgMsg.getMessageText());
        }
        if (cfgMsg.getSeverity() != Levels.UNKNOWN) {
            msg.setSeverity(cfgMsg.getSeverity());
        }
    }

    private static boolean isEmpty(final String messageText) {
        return messageText == null || messageText.trim().length() == 0;
    }
}
