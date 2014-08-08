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

public class CFStructElementExpression implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> key;
	private CFExpression value;
	
	public CFStructElementExpression( ArrayList<String> _key, CFExpression _value ) {
		key = _key;
		value = _value;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( key.get( 0 ) );
		for (int i = 1; i < key.size(); i++) {
			sb.append('.');
			sb.append( key.get( i ) );
		}
		sb.append(':');
		sb.append(value.Decompile(0));
		return sb.toString();
	}
	
}
