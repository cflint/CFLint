package com.parser.main.cfmentat.tag;

import net.htmlparser.jericho.EndTagType;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagTypeGenericImplementation;
import net.htmlparser.jericho.Tag;

final class StartTagTypeCFMLScript extends StartTagTypeGenericImplementation {
	protected static final StartTagTypeCFMLScript INSTANCE = new StartTagTypeCFMLScript();
	
	private StartTagTypeCFMLScript() {
		super("CFML script", "<cfscript", ">", EndTagType.NORMAL, false, false, false);
	}
	
	protected Tag constructTagAt(final Source source, final int pos) {
		final StartTag startTag = (StartTag) super.constructTagAt(source, pos);
		if (startTag == null)
			return null;
		// A CFML script element requires the attribute language="php".
		// if
		// (!"php".equalsIgnoreCase(startTag.getAttributes().getValue("language")))
		// return null;
		return startTag;
	}
}
