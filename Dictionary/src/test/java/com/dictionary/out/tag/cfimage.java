
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfimage extends StartTagTypeGenericImplementation {
	protected static final cfimage INSTANCE = new cfimage();

	private cfimage() {
		super("CFML if tag", "<cfimage", ">", EndTagType.NORMAL, true, true, false);
	}

}
					