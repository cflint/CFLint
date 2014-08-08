
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfntauthenticate extends StartTagTypeGenericImplementation {
	protected static final cfntauthenticate INSTANCE = new cfntauthenticate();

	private cfntauthenticate() {
		super("CFML if tag", "<cfntauthenticate", ">", EndTagType.NORMAL, true, true, false);
	}

}
					