
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfftp extends StartTagTypeGenericImplementation {
	protected static final cfftp INSTANCE = new cfftp();

	private cfftp() {
		super("CFML if tag", "<cfftp", ">", EndTagType.NORMAL, true, true, false);
	}

}
					