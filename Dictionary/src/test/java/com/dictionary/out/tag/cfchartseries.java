
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfchartseries extends StartTagTypeGenericImplementation {
	protected static final cfchartseries INSTANCE = new cfchartseries();

	private cfchartseries() {
		super("CFML if tag", "<cfchartseries", ">", EndTagType.NORMAL, true, true, false);
	}

}
					