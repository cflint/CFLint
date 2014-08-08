
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfexchangecalendar extends StartTagTypeGenericImplementation {
	protected static final cfexchangecalendar INSTANCE = new cfexchangecalendar();

	private cfexchangecalendar() {
		super("CFML if tag", "<cfexchangecalendar", ">", EndTagType.NORMAL, true, true, false);
	}

}
					