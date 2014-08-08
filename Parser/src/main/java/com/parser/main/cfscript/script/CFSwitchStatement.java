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
 * This class represents a switch statement. 
 */

import java.util.List;

import org.antlr.runtime.Token;

import com.parser.main.cfscript.CFExpression;

public class CFSwitchStatement extends CFParsedStatement implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<CFCase> cases;
	private CFExpression variable;
	
	public CFSwitchStatement(Token _token, CFExpression _variable, List<CFCase> _cases) {
		super(_token);
		cases = _cases;
		variable = _variable;
	}
	
	public void checkIndirectAssignments(String[] scriptSource) {
		for (int i = 0; i < cases.size(); i++) {
			cases.get(i).checkIndirectAssignments(scriptSource);
		}
	}
	
	public String Decompile(int _indent) {
		StringBuilder sb = new StringBuilder();
		sb.append("switch (");
		sb.append(variable.Decompile(0));
		sb.append("){\n");
		for (int i = 0; i < cases.size(); i++) {
			sb.append(((CFCase) cases.get(i)).Decompile(0));
		}
		sb.append("\n}");
		return sb.toString();
	}
	
}
