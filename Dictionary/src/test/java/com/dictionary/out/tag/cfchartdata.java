
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfchartdata extends StartTagTypeGenericImplementation {
	protected static final cfchartdata INSTANCE = new cfchartdata();

	private cfchartdata() {
		super("CFML if tag", "<cfchartdata", ">", EndTagType.NORMAL, true, true, false);
	}

}
					