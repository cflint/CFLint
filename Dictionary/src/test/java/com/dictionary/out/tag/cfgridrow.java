
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfgridrow extends StartTagTypeGenericImplementation {
	protected static final cfgridrow INSTANCE = new cfgridrow();

	private cfgridrow() {
		super("CFML if tag", "<cfgridrow", ">", EndTagType.NORMAL, true, true, false);
	}

}
					