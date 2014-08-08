
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfldap extends StartTagTypeGenericImplementation {
	protected static final cfldap INSTANCE = new cfldap();

	private cfldap() {
		super("CFML if tag", "<cfldap", ">", EndTagType.NORMAL, true, true, false);
	}

}
					