package com.parser.main.cfmentat.tag;

// note this has the same startdelimiter as processing instruction, so overrides it if registered
final class StartTagTypeCfElse extends CFMLStartTag {
	protected static final StartTagTypeCfElse INSTANCE = new StartTagTypeCfElse();
	
	private StartTagTypeCfElse() {
		// super("CFSET","<cfset",">",null,true,false,false);
		super("CFML short tag", "<cfelse", ">", null, false);
	}
}
