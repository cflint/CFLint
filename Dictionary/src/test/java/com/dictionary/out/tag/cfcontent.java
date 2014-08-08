
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfcontent extends StartTagTypeGenericImplementation {
	protected static final cfcontent INSTANCE = new cfcontent();

	private cfcontent() {
		super("CFML if tag", "<cfcontent", ">", EndTagType.NORMAL, true, true, false);
	}

}
					