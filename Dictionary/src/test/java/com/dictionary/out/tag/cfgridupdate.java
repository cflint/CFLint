
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfgridupdate extends StartTagTypeGenericImplementation {
	protected static final cfgridupdate INSTANCE = new cfgridupdate();

	private cfgridupdate() {
		super("CFML if tag", "<cfgridupdate", ">", EndTagType.NORMAL, true, true, false);
	}

}
					