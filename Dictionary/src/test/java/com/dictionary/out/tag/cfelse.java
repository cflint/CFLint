
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfelse extends StartTagTypeGenericImplementation {
	protected static final cfelse INSTANCE = new cfelse();

	private cfelse() {
		super("CFML if tag", "<cfelse", ">", EndTagType.NORMAL, true, true, false);
	}

}
					