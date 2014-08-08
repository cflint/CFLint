
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfset extends StartTagTypeGenericImplementation {
	protected static final cfset INSTANCE = new cfset();

	private cfset() {
		super("CFML if tag", "<cfset", ">", EndTagType.NORMAL, true, true, false);
	}

}
					