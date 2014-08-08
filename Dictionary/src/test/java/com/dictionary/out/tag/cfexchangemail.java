
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfexchangemail extends StartTagTypeGenericImplementation {
	protected static final cfexchangemail INSTANCE = new cfexchangemail();

	private cfexchangemail() {
		super("CFML if tag", "<cfexchangemail", ">", EndTagType.NORMAL, true, true, false);
	}

}
					