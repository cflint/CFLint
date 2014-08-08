
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfthrow extends StartTagTypeGenericImplementation {
	protected static final cfthrow INSTANCE = new cfthrow();

	private cfthrow() {
		super("CFML if tag", "<cfthrow", ">", EndTagType.NORMAL, true, true, false);
	}

}
					