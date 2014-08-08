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

import java.util.Set;
import java.util.Vector;

public class ArgumentsVector extends Vector<CFExpression> {
	
	private static final long serialVersionUID = 1L;
	
	private SequencedHashMap namedArgs;
	
	public ArgumentsVector() {
		super();
		namedArgs = new SequencedHashMap();
	}
	
	public void putNamedArg(String _name, CFExpression _val) {
		namedArgs.put(_name.toLowerCase(), _val);
		addElement(_val);
	}
	
	public CFExpression getNamedArg(String _name) {
		return (CFExpression) namedArgs.get(_name.toLowerCase());
	}
	
	public Set<String> keys() {
		return (Set<String>) namedArgs.keySet();
	}
	
}
