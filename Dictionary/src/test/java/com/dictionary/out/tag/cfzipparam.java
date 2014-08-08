
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfzipparam extends StartTagTypeGenericImplementation {
	protected static final cfzipparam INSTANCE = new cfzipparam();

	private cfzipparam() {
		super("CFML if tag", "<cfzipparam", ">", EndTagType.NORMAL, true, true, false);
	}

}
					