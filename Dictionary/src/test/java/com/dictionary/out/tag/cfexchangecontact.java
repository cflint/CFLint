
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfexchangecontact extends StartTagTypeGenericImplementation {
	protected static final cfexchangecontact INSTANCE = new cfexchangecontact();

	private cfexchangecontact() {
		super("CFML if tag", "<cfexchangecontact", ">", EndTagType.NORMAL, true, true, false);
	}

}
					