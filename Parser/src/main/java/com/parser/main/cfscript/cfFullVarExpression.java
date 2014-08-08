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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Vector;

import org.antlr.runtime.Token;

// NOTE it's important not to do import com.naryx.tagfusion.cfm.script.*;
// because of duplicate class names
import com.parser.main.cfscript.script.userDefinedFunction;

public class cfFullVarExpression extends CFVarExpression implements Serializable {
	
	private static final long serialVersionUID = 1;
	
	private StringBuilder image;
	private Token token;
	private List<CFExpression> expressions;
	
	// if true, this variable references the client or cookie scope. These
	// scopes
	// cannot contain structs but can contain variables with names containing
	// periods.
	private boolean isCookieClientVariable;
	
	/**
	 * The operator list is represented as a BitSet since there are only 2 operators when it comes to the variables -
	 * the '.' and the '[' '.' is represented as 1 and '[' is represented as 0 in the BitSet
	 */
	private BitSet operators;
	
	private static final boolean DOT = true, LBRACKET = false;
	private int exprSize;
	
	public cfFullVarExpression(Token _t, CFExpression _main, String _image) {
		super(_t);
		token = _t;
		image = new StringBuilder(_image);
		expressions = new ArrayList<CFExpression>();
		expressions.add(_main);
		operators = new BitSet(8);
		exprSize = 0;
		
		isCookieClientVariable = false;
		if (_main instanceof CFIdentifier) {
			String name = ((CFIdentifier) _main).getName().toLowerCase();
			if (name.equals("cookie") || name.equals("client")) {
				isCookieClientVariable = true;
			}
		}
	}
	
	public byte getType() {
		return expressions.get(expressions.size() - 1).getType();
	}
	
	public boolean isEscapeSingleQuotes() {
		return expressions.get(expressions.size() - 1).isEscapeSingleQuotes();
	}
	
	public void addBracketOperation(CFExpression _right) {
		// if this is a cookie/client var we want to combine the struct key
		expressions.add(_right);
		exprSize++;
		image.append('[');
		image.append(_right.Decompile(0));
		image.append(']');
	}
	
	public void addDotOperation(CFExpression _right) {
		if (isCookieClientVariable) { // a client/cookie expression with only
			// '.'s
			// should be at most 2 subexpressions -
			// 'cookie' and variable name in the cookie
			// scope
			if (expressions.size() == 2 && operators.get(1) == DOT) {
				CFExpression exp = expressions.get(1);
				expressions.remove(1);
				String subImage = exp.Decompile(0) + "." + _right.Decompile(0);
				expressions.add(new CFIdentifier(token, subImage.toString()));
			} else {
				expressions.add(_right);
				exprSize++;
				operators.set(exprSize);
				image.append('.');
				image.append(_right.Decompile(0));
			}
			
		} else {
			expressions.add(_right);
			exprSize++;
			operators.set(exprSize);
			image.append('.');
			image.append(_right.Decompile(0));
		}
	}
	
	public String Decompile(int indent) {
		return image.toString();
	}
	
	public List<CFExpression> getExpressions() {
		return expressions;
	}
	
}
