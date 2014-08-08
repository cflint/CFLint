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
 * This class represents a case/default label in a switch statement. A cfswitchstatement should
 * hold a vector of these.
 */

import java.util.Iterator;
import java.util.List;

import com.parser.main.cfscript.CFExpression;

public class CFCase implements CFScriptStatement, java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<CFScriptStatement> statementBlock;
	private boolean isDefault = true;
	private CFExpression constant;
	
	public CFCase(CFExpression _constant, List<CFScriptStatement> _statementBlock) {
		this(_statementBlock);
		isDefault = false;
		constant = _constant;
	}
	
	public CFCase(List<CFScriptStatement> _statementBlock) {
		statementBlock = _statementBlock;
	}
	
	public boolean isDefault() {
		return isDefault;
	}
	
	public void checkIndirectAssignments(String[] scriptSource) {
		for (int i = 0; i < statementBlock.size(); i++) {
			statementBlock.get(i).checkIndirectAssignments(scriptSource);
		}
	}
	
	public String toString() {
		String temp = "case :\n";
		Iterator<CFScriptStatement> statements = statementBlock.iterator();
		
		while (statements.hasNext())
			temp += statements.next().Decompile(0) + "\n";
		
		return temp;
	}
	
	public String Decompile(int indent) {
		StringBuilder sb = new StringBuilder();
		if (isDefault) {
			sb.append("default:");
		} else {
			sb.append("case ");
			sb.append(constant.Decompile(0));
		}
		return sb.toString();
	}
	
}
