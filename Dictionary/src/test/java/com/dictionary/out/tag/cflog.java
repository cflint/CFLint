
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cflog extends StartTagTypeGenericImplementation {
	protected static final cflog INSTANCE = new cflog();

	private cflog() {
		super("CFML if tag", "<cflog", ">", EndTagType.NORMAL, true, true, false);
	}

}
					