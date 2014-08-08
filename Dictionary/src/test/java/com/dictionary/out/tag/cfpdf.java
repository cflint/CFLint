
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfpdf extends StartTagTypeGenericImplementation {
	protected static final cfpdf INSTANCE = new cfpdf();

	private cfpdf() {
		super("CFML if tag", "<cfpdf", ">", EndTagType.NORMAL, true, true, false);
	}

}
					