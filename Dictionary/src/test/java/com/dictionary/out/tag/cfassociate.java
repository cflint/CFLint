
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfassociate extends StartTagTypeGenericImplementation {
	protected static final cfassociate INSTANCE = new cfassociate();

	private cfassociate() {
		super("CFML if tag", "<cfassociate", ">", EndTagType.NORMAL, true, true, false);
	}

}
					