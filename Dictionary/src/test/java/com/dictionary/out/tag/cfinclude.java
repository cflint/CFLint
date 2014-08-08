
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfinclude extends StartTagTypeGenericImplementation {
	protected static final cfinclude INSTANCE = new cfinclude();

	private cfinclude() {
		super("CFML if tag", "<cfinclude", ">", EndTagType.NORMAL, true, true, false);
	}

}
					