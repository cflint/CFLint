
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfabort extends StartTagTypeGenericImplementation {
	protected static final cfabort INSTANCE = new cfabort();

	private cfabort() {
		super("CFML if tag", "<cfabort", ">", EndTagType.NORMAL, true, true, false);
	}

}
					