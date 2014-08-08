
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfapplet extends StartTagTypeGenericImplementation {
	protected static final cfapplet INSTANCE = new cfapplet();

	private cfapplet() {
		super("CFML if tag", "<cfapplet", ">", EndTagType.NORMAL, true, true, false);
	}

}
					