
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfsprydataset extends StartTagTypeGenericImplementation {
	protected static final cfsprydataset INSTANCE = new cfsprydataset();

	private cfsprydataset() {
		super("CFML if tag", "<cfsprydataset", ">", EndTagType.NORMAL, true, true, false);
	}

}
					