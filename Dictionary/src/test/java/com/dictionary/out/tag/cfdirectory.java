
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfdirectory extends StartTagTypeGenericImplementation {
	protected static final cfdirectory INSTANCE = new cfdirectory();

	private cfdirectory() {
		super("CFML if tag", "<cfdirectory", ">", EndTagType.NORMAL, true, true, false);
	}

}
					