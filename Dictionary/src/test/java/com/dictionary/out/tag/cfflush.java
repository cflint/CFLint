
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfflush extends StartTagTypeGenericImplementation {
	protected static final cfflush INSTANCE = new cfflush();

	private cfflush() {
		super("CFML if tag", "<cfflush", ">", EndTagType.NORMAL, true, true, false);
	}

}
					