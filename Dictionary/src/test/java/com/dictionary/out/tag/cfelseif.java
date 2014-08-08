
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfelseif extends StartTagTypeGenericImplementation {
	protected static final cfelseif INSTANCE = new cfelseif();

	private cfelseif() {
		super("CFML if tag", "<cfelseif", ">", EndTagType.NORMAL, true, true, false);
	}

}
					