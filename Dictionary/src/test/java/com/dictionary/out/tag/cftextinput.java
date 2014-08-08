
package com.dictionary.out.tag;

import au.id.jericho.lib.html.EndTagType;
import au.id.jericho.lib.html.ParseText;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.StartTagTypeGenericImplementation;
import au.id.jericho.lib.html.Tag;

final class cftextinput extends StartTagTypeGenericImplementation {
	protected static final cftextinput INSTANCE = new cftextinput();

	private cftextinput() {
		super("CFML if tag", "<cftextinput", ">", EndTagType.NORMAL, true, true, false);
	}

}
					