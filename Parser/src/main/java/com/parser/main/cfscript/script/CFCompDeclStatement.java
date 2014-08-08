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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.antlr.runtime.Token;

import com.parser.main.cfscript.CFContext;
import com.parser.main.cfscript.CFExpression;
import com.parser.main.cfscript.CFLiteral;
import com.parser.main.cfscript.ParseException;

public class CFCompDeclStatement extends CFParsedStatement {
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> attributes;
	private CFScriptStatement body;
	
	private byte access;
	private String returnType;
	
	// TODO: prevent function declared inside function. May want to do this elsewhere
	public CFCompDeclStatement(Token _t, Map<String, CFExpression> _attr, CFScriptStatement _body) {
		super(_t);
		body = _body;
		
		// handle the function attributes
		attributes = new HashMap<String, String>();
		if (_attr != null) {
			Iterator<String> keys = _attr.keySet().iterator();
			while (keys.hasNext()) {
				String nextKey = keys.next();
				CFExpression nextExpr = _attr.get(nextKey);
				if (!(nextExpr instanceof CFLiteral)) {
					throw new ParseException(_t, "The attribute " + nextKey.toUpperCase()
							+ " must have a constant value");
				}
				
				attributes.put(nextKey, ((CFLiteral) nextExpr).getStringImage());
			}
		}
		
	}
	
	public CFScriptStatement getBody() {
		return body;
	}
	
	public void checkIndirectAssignments(String[] scriptSource) {
		body.checkIndirectAssignments(scriptSource);
	}
	
	public CFStatementResult Exec(CFContext context) {
		return null;
	}
	
	public String Decompile(int indent) {
		StringBuilder sb = new StringBuilder();
		sb.append(Indent(indent));
		sb.append(" component ");
		sb.append(body.Decompile(indent + 2));
		sb.append("\n");
		sb.append(Indent(indent));
		sb.append("}");
		
		return sb.toString();
	}
}
