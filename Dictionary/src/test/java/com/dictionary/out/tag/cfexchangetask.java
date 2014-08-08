
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfexchangetask extends StartTagTypeGenericImplementation {
	protected static final cfexchangetask INSTANCE = new cfexchangetask();

	private cfexchangetask() {
		super("CFML if tag", "<cfexchangetask", ">", EndTagType.NORMAL, true, true, false);
	}

}
					