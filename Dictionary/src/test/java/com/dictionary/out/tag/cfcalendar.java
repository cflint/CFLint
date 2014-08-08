
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cfcalendar extends StartTagTypeGenericImplementation {
	protected static final cfcalendar INSTANCE = new cfcalendar();

	private cfcalendar() {
		super("CFML if tag", "<cfcalendar", ">", EndTagType.NORMAL, true, true, false);
	}

}
					