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

/**
 * Compound statement, a container for a sequence of substatements.
 */

import java.util.ArrayList;

public class CFCompoundStatement extends CFParsedStatement implements CFScriptStatement, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<CFScriptStatement> _v; // Vector of CFStatements
	
	public CFCompoundStatement() {
		super(1, 1);
		_v = new ArrayList<CFScriptStatement>();
	}
	
	public CFCompoundStatement(org.antlr.runtime.Token t) {
		super(t);
		_v = new ArrayList<CFScriptStatement>();
	}
	
	public void add(CFScriptStatement s) {
		_v.add(s);
	}
	
	public void addFunction(CFScriptStatement s) {
		_v.add(0, s);
	}
	
	public ArrayList<CFScriptStatement> getStatements() {
		return _v;
	}
	
	public int numOfStatements() {
		return _v.size();
	}
	
	public void checkIndirectAssignments(String[] scriptSource) {
		for (int i = 0; i < _v.size(); i++) {
			_v.get(i).checkIndirectAssignments(scriptSource);
		}
	}
	
	public String Decompile(int indent) {
		StringBuilder s = new StringBuilder();
		s.append(Indent(indent));
		s.append("{\n");
		for (int i = 0; i < _v.size(); i++) {
			s.append(_v.get(i).Decompile(indent + 2));
		}
		s.append("\n");
		s.append(Indent(indent));
		s.append("}");
		return s.toString();
	}
	
}
