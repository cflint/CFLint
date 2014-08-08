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

package com.parser.main.cfscript;

import org.antlr.runtime.Token;

public class CFAssignmentExpression extends CFExpression {
	
	private static final long serialVersionUID = 1L;
	
	private CFExpression left;
	private CFExpression right;
	
	private int type;
	
	public CFAssignmentExpression(Token t, CFExpression _left, CFExpression _right) {
		super(t);
		left = _left;
		right = _right;
		type = t.getType();
	}
	
	public byte getType() {
		return CFExpression.ASSIGNMENT;
	}
	
	public void checkIndirect(String expr) {
		if (left instanceof CFVarExpression) {
			String lhs = expr.substring(0, expr.indexOf('=')).trim();
			// check for special case of "#foo#"="bar" or '#foo#'='bar'
			if ((lhs.startsWith("\"#") && lhs.endsWith("#\"")) || (lhs.startsWith("'#") && lhs.endsWith("#'"))) {
				((CFVarExpression) left).setIndirect(true);
			}
		}
	}
	
	public void checkIndirectAssignments(String[] scriptSource) {
		if (left instanceof CFVarExpression) {
			/*
			 * We know this is a valid assignment expression but is the LHS expression a # expression e.g. "#foo#" =
			 * "bar" Since the poundSignFilter removes the #'s for the parser we need to look at the original source. We
			 * can get the line from this CFExpression but the column isn't accurate enough for CFFullVarExpressions
			 * especially as more than one expression may occur on the same line.
			 * 
			 * We look backwards from the '=' skipping any whitespace and looking for #", #' or # on it's own.
			 */
			
			// find the original expression in the script source code
			String expr = scriptSource[left.getLine() - 1];
			int lhsEnd = expr.indexOf("=", left.getColumn());
			if (lhsEnd == -1) {
				// if the expression is split over 2 lines
				// e.g. "#foo#"
				// = "bar"
				lhsEnd = expr.length();
			}
			
			char[] exprChars = expr.substring(0, lhsEnd).toCharArray();
			int i = exprChars.length - 1;
			// skip over whitespace
			while (i >= 0 && (Character.isWhitespace(exprChars[i]))) {
				i--;
			}
			
			// does LHS expression end in #" or #'?
			if (i >= 0 && (exprChars[i] == '\"' || exprChars[i] == '\'')) {
				i--;
				if (i >= 0 && exprChars[i] == '#') {
					((CFVarExpression) left).setIndirect(true);
				}
			}
		}
	}
	
	public String Decompile(int indent) {
		return left.Decompile(0) + "=" + right.Decompile(0);
	}
	
	public CFExpression getLeft() {
		return left;
	}
	
	public CFExpression getRight() {
		return right;
	}
}
