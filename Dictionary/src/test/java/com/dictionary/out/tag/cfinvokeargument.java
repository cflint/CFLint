
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfinvokeargument extends StartTagTypeGenericImplementation {
	protected static final cfinvokeargument INSTANCE = new cfinvokeargument();

	private cfinvokeargument() {
		super("CFML if tag", "<cfinvokeargument", ">", EndTagType.NORMAL, true, true, false);
	}

}
					