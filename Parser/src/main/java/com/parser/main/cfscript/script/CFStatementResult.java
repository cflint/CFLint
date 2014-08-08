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

import com.parser.main.cfscript.cfData;

public class CFStatementResult {
	
	private static final int RETURN_TYPE = 0;
	private static final int BREAK_TYPE = 1;
	private static final int CONTINUE_TYPE = 2;
	
	public static final CFStatementResult BREAK = new CFStatementResult(BREAK_TYPE);
	public static final CFStatementResult CONTINUE = new CFStatementResult(CONTINUE_TYPE);
	
	private int resultType;
	private cfData returnValue;
	
	private CFStatementResult(int type) {
		resultType = type;
	}
	
	public CFStatementResult(cfData value) {
		resultType = RETURN_TYPE;
		returnValue = value;
	}
	
	public boolean isReturn() {
		return (resultType == RETURN_TYPE);
	}
	
	public boolean isBreak() {
		return (resultType == BREAK_TYPE);
	}
	
	public boolean isContinue() {
		return (resultType == CONTINUE_TYPE);
	}
	
	public cfData getReturnValue() {
		return returnValue;
	}
}
