
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfslider extends StartTagTypeGenericImplementation {
	protected static final cfslider INSTANCE = new cfslider();

	private cfslider() {
		super("CFML if tag", "<cfslider", ">", EndTagType.NORMAL, true, true, false);
	}

}
					