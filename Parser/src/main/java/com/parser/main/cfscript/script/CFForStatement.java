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

import org.antlr.runtime.Token;

import com.parser.main.cfscript.CFExpression;

public class CFForStatement extends CFParsedStatement implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private CFExpression init;
	private CFExpression cond;
	private CFExpression next;
	private CFScriptStatement body;
	
	public CFForStatement(Token _t, CFExpression _init, CFExpression _cond, CFExpression _next, CFScriptStatement _body) {
		super(_t);
		init = _init;
		cond = _cond;
		next = _next;
		body = _body;
	}
	
	public void checkIndirectAssignments(String[] scriptSource) {
		body.checkIndirectAssignments(scriptSource);
	}
	
	public String Decompile(int indent) {
		String s = Indent(indent) + "for(";
		if (init != null) {
			s += init.Decompile(indent);
		}
		s += ";";
		if (cond != null) {
			s += cond.Decompile(indent);
		}
		s += ";";
		if (next != null) {
			s += next.Decompile(indent);
		}
		body.Decompile(indent + 2);
		
		return s;
	}
	
	public CFExpression getInit() {
		return init;
	}
	
	public CFExpression getCond() {
		return cond;
	}
	
	public CFExpression getNext() {
		return next;
	}
	
	public CFScriptStatement getBody() {
		return body;
	}
	
}

