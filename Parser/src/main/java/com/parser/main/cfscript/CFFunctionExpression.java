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

import com.parser.main.cfscript.script.userDefinedFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

	
public class CFFunctionExpression extends CFExpression {
	private static final long serialVersionUID = 1L;

	private String name;
	private Vector<CFExpression> args; // Vector of CFExpression's
	private boolean isUDF = true;
	private boolean isParamExists;

	public CFFunctionExpression(CFIdentifier _name, Vector<CFExpression> _args)
			throws ParseException {
		super(_name.getToken());
		name = _name.getName().toLowerCase();
		args = _args;
		isParamExists = name.equals("parameterexists");
		// isUDF = !expressionEngine.isFunction(name);
		isUDF = false;

		// if it's a predefined function, check the number of params is legal
		if (!isUDF) {
			// try {
			// com.naryx.tagfusion.expression.function.functionBase f =
			// com.naryx.tagfusion.expression.compile.expressionEngine
			// .getFunction(name);
			// if ( _args.size() < f.getMin() )
			// throw new ParseException(_name.getToken(), "The function " + name
			// + " requires at least " + f.getMin() + " argument(s).");
			// if ( _args.size() > f.getMax() )
			// throw new ParseException(_name.getToken(), "The function " + name
			// + " requires at most " + f.getMax() + " argument(s).");
			//
			// } catch (com.naryx.tagfusion.cfm.engine.cfmRunTimeException ee) {
			// // shouldn't happen
			// }
		}

	}

	public byte getType() {
		return CFExpression.FUNCTION;
	}

	public String getFunctionName() {
		return name;
	}

	public boolean isUDF() {
		return isUDF;
	}

	public String Decompile(int indent) {
		String s = name + "(";

		for (int i = 0; i < args.size(); i++) {
			s += args.elementAt(i).Decompile(indent);
			if (i < args.size() - 1) {
				s += ", ";
			}
		}

		s += ")";

		return s;
	}

	public Vector<CFExpression> getArgs() {
		return args;
	}

	public String getName() {
		return name;
	}

}
