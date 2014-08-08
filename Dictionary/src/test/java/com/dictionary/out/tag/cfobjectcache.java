
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfobjectcache extends StartTagTypeGenericImplementation {
	protected static final cfobjectcache INSTANCE = new cfobjectcache();

	private cfobjectcache() {
		super("CFML if tag", "<cfobjectcache", ">", EndTagType.NORMAL, true, true, false);
	}

}
					