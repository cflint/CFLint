
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfajaxproxy extends StartTagTypeGenericImplementation {
	protected static final cfajaxproxy INSTANCE = new cfajaxproxy();

	private cfajaxproxy() {
		super("CFML if tag", "<cfajaxproxy", ">", EndTagType.NORMAL, true, true, false);
	}

}
					