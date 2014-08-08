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

public class CFIdentifier extends CFVarExpression implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected Token token;
	
	public CFIdentifier(Token _t) {
		super(_t);
		name = _t.getText();
		token = _t;
	}
	
	public CFIdentifier(Token _t, String _img) {
		super(_t);
		name = _img;
		token = _t;
	}
	
	public byte getType() {
		return CFExpression.IDENTIFIER;
	}
	
	public String getName() {
		return name;
	}
	
	public Token getToken() {
		return token;
	}
	
	public String Decompile(int indent) {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
}
