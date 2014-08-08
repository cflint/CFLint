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

import java.util.List;

import org.antlr.runtime.Token;

import com.parser.main.cfscript.ParseException;

public class CFTryCatchStatement extends CFParsedStatement implements java.io.Serializable {
	
	private static final long serialVersionUID = 1;
	
	private CFScriptStatement body; // body of the try block
	private List<cfCatchClause> catchStatements;
	private CFScriptStatement finallyStatement;
	
	public CFTryCatchStatement(Token _t1, CFScriptStatement _s1, List<cfCatchClause> _catches,
			CFScriptStatement _finally) {
		super(_t1);
		body = _s1;
		catchStatements = _catches;
		
		if (catchStatements.size() == 0 && _finally == null) {
			throw new ParseException(_t1, "try statement must include at least one catch clause or a finally clause.");
		}
		
		// now stick the catch 'any' if there is one at the end
		cfCatchClause catchAny = null;
		cfCatchClause nextClause;
		for (int i = 0; i < catchStatements.size(); i++) {
			nextClause = catchStatements.get(i);
			if (nextClause.isCatchAny()) {
				catchAny = nextClause;
				catchStatements.remove(i);
				i--; // make adjustment for removed clause
			}
		}
		
		if (catchAny != null) {
			catchStatements.add(catchAny);
		}
		
		finallyStatement = _finally;
	}
	
	public void checkIndirectAssignments(String[] scriptSource) {
		body.checkIndirectAssignments(scriptSource);
		for (int i = 0; i < catchStatements.size(); i++) {
			((CFScriptStatement) catchStatements.get(i)).checkIndirectAssignments(scriptSource);
		}
	}
	
	public String Decompile(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < catchStatements.size(); i++) {
			CFCatchStatement clause = (CFCatchStatement) catchStatements.get(i);
			sb.append("catch(");
			sb.append(clause.getType());
			sb.append(" ");
			sb.append(clause.getVariable());
			sb.append("{");
			sb.append(clause.getCatchBody().Decompile(0));
			sb.append("}");
		}
		
		sb.insert(0, "try{" + body.Decompile(0) + "}");
		return sb.toString();
	}
	
}
