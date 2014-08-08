
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfregistry extends StartTagTypeGenericImplementation {
	protected static final cfregistry INSTANCE = new cfregistry();

	private cfregistry() {
		super("CFML if tag", "<cfregistry", ">", EndTagType.NORMAL, true, true, false);
	}

}
					