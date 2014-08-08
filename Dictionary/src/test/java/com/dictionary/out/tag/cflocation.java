
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cflocation extends StartTagTypeGenericImplementation {
	protected static final cflocation INSTANCE = new cflocation();

	private cflocation() {
		super("CFML if tag", "<cflocation", ">", EndTagType.NORMAL, true, true, false);
	}

}
					