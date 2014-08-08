
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfcookie extends StartTagTypeGenericImplementation {
	protected static final cfcookie INSTANCE = new cfcookie();

	private cfcookie() {
		super("CFML if tag", "<cfcookie", ">", EndTagType.NORMAL, true, true, false);
	}

}
					