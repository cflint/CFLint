
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfsetting extends StartTagTypeGenericImplementation {
	protected static final cfsetting INSTANCE = new cfsetting();

	private cfsetting() {
		super("CFML if tag", "<cfsetting", ">", EndTagType.NORMAL, true, true, false);
	}

}
					