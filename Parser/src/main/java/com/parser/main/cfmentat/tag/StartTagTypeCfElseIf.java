package com.parser.main.cfmentat.tag;

// note this has the same startdelimiter as processing instruction, so overrides it if registered
final class StartTagTypeCfElseIf extends CFMLStartTag {
	protected static final StartTagTypeCfElseIf INSTANCE = new StartTagTypeCfElseIf();
	
	private StartTagTypeCfElseIf() {
		// super("CFSET","<cfset",">",null,true,false,false);
		super("CFML short tag", "<cfelse", ">", null, false, false, true);
	}
}
