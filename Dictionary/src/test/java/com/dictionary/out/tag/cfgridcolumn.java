
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfgridcolumn extends StartTagTypeGenericImplementation {
	protected static final cfgridcolumn INSTANCE = new cfgridcolumn();

	private cfgridcolumn() {
		super("CFML if tag", "<cfgridcolumn", ">", EndTagType.NORMAL, true, true, false);
	}

}
					