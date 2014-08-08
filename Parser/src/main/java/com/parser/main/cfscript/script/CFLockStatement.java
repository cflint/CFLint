/* 
 *  Copyright (C) 2000 - 2010 TagServlet Ltd
 *
 *  This file is part of Open BlueDragon (OpenBD) CFML Server Engine.
 *  
 *  OpenBD is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  Free Software Foundation,version 3.
 *  
 *  OpenBD is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with OpenBD.  If not, see http://www.gnu.org/licenses/
 *  
 *  Additional permission under GNU GPL version 3 section 7
 *  
 *  If you modify this Program, or any covered work, by linking or combining 
 *  it with any of the JARS listed in the README.txt (or a modified version of 
 *  (that library), containing parts covered by the terms of that JAR, the 
 *  licensors of this Program grant you additional permission to convey the 
 *  resulting work. 
 *  README.txt @ http://www.openbluedragon.org/license/README.txt
 *  
 *  http://www.openbluedragon.org/
 */

package com.parser.main.cfscript.script;

import java.util.HashSet;
import java.util.Map;

import com.parser.main.cfscript.CFExpression;
import com.parser.main.cfscript.ParseException;

public class CFLockStatement extends CFParsedAttributeStatement implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private CFScriptStatement body;
	
	private static HashSet<String> supportedAttributes;
	
	static {
		supportedAttributes = new HashSet<String>();
		supportedAttributes.add("TYPE");
		supportedAttributes.add("TIMEOUT");
		supportedAttributes.add("NAME");
		supportedAttributes.add("SCOPE");
		supportedAttributes.add("THROWONTIMEOUT");
	}
	
	public CFLockStatement(org.antlr.runtime.Token _t, Map<String, CFExpression> _attr, CFScriptStatement _body) {
		super(_t, _attr);
		
		validateAttributes(_t, supportedAttributes);
		
		// minimal requirement is the timeout attribute
		if (!containsAttribute("TIMEOUT"))
			throw new ParseException(_t, "Lock requires the TIMEOUT attribute");
		
		if (containsAttribute("NAME") && containsAttribute("SCOPE"))
			throw new ParseException(_t, "Invalid Attributes: specify either SCOPE or NAME, but not both");
		
		body = _body;
	}
	
	@Override
	public String Decompile(int indent) {
		StringBuilder sb = new StringBuilder();
		sb.append("lock ");
		DecompileAttributes(sb);
		sb.append(body.Decompile(0));
		
		return sb.toString();
	}
	
}
