package com.cflint.tools;

import java.lang.reflect.Field;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFUnaryExpression;
import net.htmlparser.jericho.Element;

public class CFTool {

	public static CFExpression sub(final CFUnaryExpression expression) {
		Field f;
		try {
			f = CFUnaryExpression.class.getDeclaredField("sub");
			f.setAccessible(true);
			return (CFExpression) (f.get(expression));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

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
}
