
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfexchangefilter extends StartTagTypeGenericImplementation {
	protected static final cfexchangefilter INSTANCE = new cfexchangefilter();

	private cfexchangefilter() {
		super("CFML if tag", "<cfexchangefilter", ">", EndTagType.NORMAL, true, true, false);
	}

}
					