
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfhtmlhead extends StartTagTypeGenericImplementation {
	protected static final cfhtmlhead INSTANCE = new cfhtmlhead();

	private cfhtmlhead() {
		super("CFML if tag", "<cfhtmlhead", ">", EndTagType.NORMAL, true, true, false);
	}

}
					