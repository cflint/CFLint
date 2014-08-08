
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cferror extends StartTagTypeGenericImplementation {
	protected static final cferror INSTANCE = new cferror();

	private cferror() {
		super("CFML if tag", "<cferror", ">", EndTagType.NORMAL, true, true, false);
	}

}
					