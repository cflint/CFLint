
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfpresenter extends StartTagTypeGenericImplementation {
	protected static final cfpresenter INSTANCE = new cfpresenter();

	private cfpresenter() {
		super("CFML if tag", "<cfpresenter", ">", EndTagType.NORMAL, true, true, false);
	}

}
					