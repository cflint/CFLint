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

/**
 * Execution context for evaluating cfscript statements and expressions.
 *   The execution context comprises the global scope list, the local scope list,
 *   the current function hierarchy, and the current source _line and _col.
 */

import java.util.Map;
import java.util.Stack;

import com.parser.main.cfscript.script.userDefinedFunction;

public class CFContext extends cfData implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private CFCallStack _callStack;
	
	private Stack<CFCallStack> callStackStack;
	
	private int _line;
	private int _col;
	
	// This is set by every expression evaluation routine - we need it for the
	// "eval()" builtin
	public cfData _lastExpr;
	
	public CFContext() {
		_callStack = new CFCallStack();
		callStackStack = new Stack<CFCallStack>();
		
		_line = 0;
		_col = 0;
		
		_lastExpr = CFUndefinedValue.UNDEFINED;
	}
	
	public CFCallStack getCallStack() {
		return _callStack;
	}
	
	public Stack<CFCallStack> getCallStackStack() {
		return callStackStack;
	}
	
	public void enterCustomTag() {
		callStackStack.push(_callStack);
		_callStack = new CFCallStack();
	}
	
	public void leaveCustomTag() {
		_callStack = callStackStack.pop();
	}
	
	/** UDF specific functions **/
	
	public boolean containsFunction(String _function) {
		return (getUDF(_function) != null);
	}
	
	private Object getUDF(String function) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void pushCall(CFCall call) {
		_callStack.push(call);
	}
	
	public void popCall() {
		_callStack.pop();
	}
	
	public boolean isCallEmpty() {
		return _callStack.isEmpty();
	}
	
	public void setLineCol(int line, int col) {
		_line = line;
		_col = col;
	}
	
	public int getLine() {
		return _line;
	}
	
	public int getCol() {
		return _col;
	}
	
	public String toString() {
		return ""; // might want to put scope print out here
	}
	
}
