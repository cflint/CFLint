
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cffile extends StartTagTypeGenericImplementation {
	protected static final cffile INSTANCE = new cffile();

	private cffile() {
		super("CFML if tag", "<cffile", ">", EndTagType.NORMAL, true, true, false);
	}

}
					