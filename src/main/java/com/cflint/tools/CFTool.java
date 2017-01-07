package com.cflint.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;

import cfml.parsing.cfscript.CFExpression;
import net.htmlparser.jericho.Element;

public class CFTool {

	public static Element getNamedParent(Element elem, final String tagName) {
		elem = elem.getParentElement();
		while (elem != null && !elem.getName().equals(tagName)) {
			elem = elem.getParentElement();
		}
		return elem;
	}

	public static boolean toBoolean(final String value) {
		if (value == null) {
			return false;
		}
		return value.trim().equalsIgnoreCase("yes") || value.trim().equalsIgnoreCase("true");
	}
	
	public static Element getElementBefore(Element element, List<Element> elements) {
		if(element != null && elements != null && elements.indexOf(element) >0){
			return elements.get(elements.indexOf(element) - 1);
		}
		return null;
	}
	
	public static Map<String, CFExpression> convertMap(Map<? extends CFExpression, CFExpression> map){
		Map<String, CFExpression> retval = new HashMap<String, CFExpression>();
		for(Entry<? extends CFExpression, CFExpression> entry: map.entrySet()){
			retval.put(entry.getKey().toString().toLowerCase(), entry.getValue());
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
		if (!isEmpty(cfgMsg.getSeverity())) {
			msg.setSeverity(cfgMsg.getSeverity());
		}
	}

	private static boolean isEmpty(final String messageText) {
		return messageText == null || messageText.trim().length() == 0;
	}
}
