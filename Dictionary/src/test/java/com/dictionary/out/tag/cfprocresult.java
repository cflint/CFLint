
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfprocresult extends StartTagTypeGenericImplementation {
	protected static final cfprocresult INSTANCE = new cfprocresult();

	private cfprocresult() {
		super("CFML if tag", "<cfprocresult", ">", EndTagType.NORMAL, true, true, false);
	}

}
					