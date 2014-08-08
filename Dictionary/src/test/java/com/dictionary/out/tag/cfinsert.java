
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfinsert extends StartTagTypeGenericImplementation {
	protected static final cfinsert INSTANCE = new cfinsert();

	private cfinsert() {
		super("CFML if tag", "<cfinsert", ">", EndTagType.NORMAL, true, true, false);
	}

}
					