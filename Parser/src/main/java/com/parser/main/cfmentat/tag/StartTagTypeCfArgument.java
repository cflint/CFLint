package com.parser.main.cfmentat.tag;

// note this has the same startdelimiter as processing instruction, so overrides it if registered
final class StartTagTypeCfArgument extends CFMLStartTag {
	protected static final StartTagTypeCfArgument INSTANCE = new StartTagTypeCfArgument();
	
	private StartTagTypeCfArgument() {
		// super("CFSET","<cfset",">",null,true,false,false);
		super("CFML short tag", "<cfargument", ">", null, false, true, false);
	}
}
