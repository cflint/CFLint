
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfproperty extends StartTagTypeGenericImplementation {
	protected static final cfproperty INSTANCE = new cfproperty();

	private cfproperty() {
		super("CFML if tag", "<cfproperty", ">", EndTagType.NORMAL, true, true, false);
	}

}
					