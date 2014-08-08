
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfrethrow extends StartTagTypeGenericImplementation {
	protected static final cfrethrow INSTANCE = new cfrethrow();

	private cfrethrow() {
		super("CFML if tag", "<cfrethrow", ">", EndTagType.NORMAL, true, true, false);
	}

}
					