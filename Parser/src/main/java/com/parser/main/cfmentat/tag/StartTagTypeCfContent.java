package com.parser.main.cfmentat.tag;

// note this has the same startdelimiter as processing instruction, so overrides it if registered
final class StartTagTypeCfContent extends CFMLStartTag {
	protected static final StartTagTypeCfContent INSTANCE = new StartTagTypeCfContent();
	
	private StartTagTypeCfContent() {
		// super("CFSET","<cfset",">",null,true,false,false);
		super("CFML short tag", "<cfcontent", ">", null, true);
	}
}
