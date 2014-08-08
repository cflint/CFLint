
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfimport extends StartTagTypeGenericImplementation {
	protected static final cfimport INSTANCE = new cfimport();

	private cfimport() {
		super("CFML if tag", "<cfimport", ">", EndTagType.NORMAL, true, true, false);
	}

}
					