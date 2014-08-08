
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfreturn extends StartTagTypeGenericImplementation {
	protected static final cfreturn INSTANCE = new cfreturn();

	private cfreturn() {
		super("CFML if tag", "<cfreturn", ">", EndTagType.NORMAL, true, true, false);
	}

}
					