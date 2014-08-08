package com.parser.main.cfmentat.tag;

import net.htmlparser.jericho.StartTagTypeGenericImplementation;

// note this has the same startdelimiter as processing instruction, so overrides it if registered
final class StartTagTypeCFMLComment extends StartTagTypeGenericImplementation {
	protected static final StartTagTypeCFMLComment INSTANCE = new StartTagTypeCFMLComment();
	
	private StartTagTypeCFMLComment() {
		super("comment", "<!---", "--->", null, false);
	}
}
