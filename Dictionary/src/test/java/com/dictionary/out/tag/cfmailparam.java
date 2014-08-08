
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfmailparam extends StartTagTypeGenericImplementation {
	protected static final cfmailparam INSTANCE = new cfmailparam();

	private cfmailparam() {
		super("CFML if tag", "<cfmailparam", ">", EndTagType.NORMAL, true, true, false);
	}

}
					