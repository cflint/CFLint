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

import java.util.ArrayList;

import org.antlr.runtime.Token;

public class CFArrayExpression extends CFExpression implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<CFExpression> elements;
	
	public CFArrayExpression(Token t) {
		super(t);
		elements = new ArrayList<CFExpression>();
	}
	
	public void addElement(CFExpression _e) {
		elements.add(_e);
	}
	
	@Override
	public String Decompile(int indent) {
		if (elements.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append('[');
			for (int i = 0; i < elements.size(); i++) {
				sb.append(((CFExpression) elements.get(i)).Decompile(0));
				sb.append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			
			sb.append(']');
			return sb.toString();
		} else {
			return "[]";
		}
	}
	
}
