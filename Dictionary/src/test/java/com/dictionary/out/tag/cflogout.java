
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cflogout extends StartTagTypeGenericImplementation {
	protected static final cflogout INSTANCE = new cflogout();

	private cflogout() {
		super("CFML if tag", "<cflogout", ">", EndTagType.NORMAL, true, true, false);
	}

}
					