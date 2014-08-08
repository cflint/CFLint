
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfloginuser extends StartTagTypeGenericImplementation {
	protected static final cfloginuser INSTANCE = new cfloginuser();

	private cfloginuser() {
		super("CFML if tag", "<cfloginuser", ">", EndTagType.NORMAL, true, true, false);
	}

}
					