
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfqueryparam extends StartTagTypeGenericImplementation {
	protected static final cfqueryparam INSTANCE = new cfqueryparam();

	private cfqueryparam() {
		super("CFML if tag", "<cfqueryparam", ">", EndTagType.NORMAL, true, true, false);
	}

}
					