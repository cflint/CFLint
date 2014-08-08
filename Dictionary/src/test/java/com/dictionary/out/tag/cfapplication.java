
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfapplication extends StartTagTypeGenericImplementation {
	protected static final cfapplication INSTANCE = new cfapplication();

	private cfapplication() {
		super("CFML if tag", "<cfapplication", ">", EndTagType.NORMAL, true, true, false);
	}

}
					