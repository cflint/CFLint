
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cftrace extends StartTagTypeGenericImplementation {
	protected static final cftrace INSTANCE = new cftrace();

	private cftrace() {
		super("CFML if tag", "<cftrace", ">", EndTagType.NORMAL, true, true, false);
	}

}
					