package com.cflint.tools;

import java.util.List;

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
}
