
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfheader extends StartTagTypeGenericImplementation {
	protected static final cfheader INSTANCE = new cfheader();

	private cfheader() {
		super("CFML if tag", "<cfheader", ">", EndTagType.NORMAL, true, true, false);
	}

}
					