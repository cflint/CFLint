
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfbreak extends StartTagTypeGenericImplementation {
	protected static final cfbreak INSTANCE = new cfbreak();

	private cfbreak() {
		super("CFML if tag", "<cfbreak", ">", EndTagType.NORMAL, true, true, false);
	}

}
					