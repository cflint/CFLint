
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfcol extends StartTagTypeGenericImplementation {
	protected static final cfcol INSTANCE = new cfcol();

	private cfcol() {
		super("CFML if tag", "<cfcol", ">", EndTagType.NORMAL, true, true, false);
	}

}
					