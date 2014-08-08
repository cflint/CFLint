
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfobject extends StartTagTypeGenericImplementation {
	protected static final cfobject INSTANCE = new cfobject();

	private cfobject() {
		super("CFML if tag", "<cfobject", ">", EndTagType.NORMAL, true, true, false);
	}

}
					