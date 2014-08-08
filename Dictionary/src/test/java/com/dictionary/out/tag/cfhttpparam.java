
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfhttpparam extends StartTagTypeGenericImplementation {
	protected static final cfhttpparam INSTANCE = new cfhttpparam();

	private cfhttpparam() {
		super("CFML if tag", "<cfhttpparam", ">", EndTagType.NORMAL, true, true, false);
	}

}
					