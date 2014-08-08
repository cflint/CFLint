
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfargument extends StartTagTypeGenericImplementation {
	protected static final cfargument INSTANCE = new cfargument();

	private cfargument() {
		super("CFML if tag", "<cfargument", ">", EndTagType.NORMAL, true, true, false);
	}

}
					