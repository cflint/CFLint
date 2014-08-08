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

public class CFLiteral extends CFExpression implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String val;
	private int kind;
	private String image;
	
	public CFLiteral(Token _t) {
		super(_t);
		kind = _t.getType();
		image = _t.getText();
		switch (kind) {
		case CFScriptLexer.FLOATING_POINT_LITERAL:
		case CFScriptLexer.INTEGER_LITERAL:
			val = _t.getText();
			break;
		case CFScriptLexer.STRING_LITERAL:
			// create a String, stripping off the surrounding quotes and
			// replacing any escaped quotes with a single quote
			String quote = _t.getText().substring(0, 1);
			String str = _t.getText().substring(1, _t.getText().length() - 1);
			str = str.replaceAll(quote + quote, quote);
			image = str;
			val = str;
			break;
		case CFScriptLexer.BOOLEAN_LITERAL:
			val = _t.getText();
			break;
		// CFML doesn't do nulls, to my knowledge
		// case CFScriptLexer.NULL:
		// val = "";
		// break;
		default:
			break;
		}
	}
	
	public byte getType() {
		return CFExpression.LITERAL;
	}
	
	public String getStringImage() {
		return image;
	}
	
	public String Decompile(int indent) {
		try {
			return val;
		} catch (Exception e) {
			return "Couldn't get literal value";
		}
		
	}
}
