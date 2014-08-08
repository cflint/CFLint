
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfwddx extends StartTagTypeGenericImplementation {
	protected static final cfwddx INSTANCE = new cfwddx();

	private cfwddx() {
		super("CFML if tag", "<cfwddx", ">", EndTagType.NORMAL, true, true, false);
	}

}
					