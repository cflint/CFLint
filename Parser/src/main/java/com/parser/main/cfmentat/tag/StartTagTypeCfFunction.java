package com.parser.main.cfmentat.tag;

import net.htmlparser.jericho.EndTagType;

// note this has the same startdelimiter as processing instruction, so overrides it if registered
final class StartTagTypeCfFunction extends CFMLStartTag {
	protected static final StartTagTypeCfFunction INSTANCE = new StartTagTypeCfFunction();
	
	private StartTagTypeCfFunction() {
		// super("CFSET","<cfset",">",null,true,false,false);
		super("CFML short tag", "<cffunction", ">", EndTagType.NORMAL, false, true, true);
	}
}
