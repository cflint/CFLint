
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfexit extends StartTagTypeGenericImplementation {
	protected static final cfexit INSTANCE = new cfexit();

	private cfexit() {
		super("CFML if tag", "<cfexit", ">", EndTagType.NORMAL, true, true, false);
	}

}
					