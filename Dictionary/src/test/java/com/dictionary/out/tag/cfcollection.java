
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfcollection extends StartTagTypeGenericImplementation {
	protected static final cfcollection INSTANCE = new cfcollection();

	private cfcollection() {
		super("CFML if tag", "<cfcollection", ">", EndTagType.NORMAL, true, true, false);
	}

}
					