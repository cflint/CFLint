package com.parser.main.cfmentat.tag;

import net.htmlparser.jericho.EndTagType;

final class StartTagTypeCfIf extends CFMLStartTag {
	protected static final StartTagTypeCfIf INSTANCE = new StartTagTypeCfIf();
	
	private StartTagTypeCfIf() {
		super("CFML if tag", "<cfif", ">", EndTagType.NORMAL, false, false, false);
	}
	
}
