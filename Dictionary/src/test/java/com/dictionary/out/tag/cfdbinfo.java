
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfdbinfo extends StartTagTypeGenericImplementation {
	protected static final cfdbinfo INSTANCE = new cfdbinfo();

	private cfdbinfo() {
		super("CFML if tag", "<cfdbinfo", ">", EndTagType.NORMAL, true, true, false);
	}

}
					