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

/**
 * Exception which can report the line number and column on which it occurred,
 * and what went wrong.
 */

package com.parser.main.cfscript;

public class CFException extends Throwable implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public int line;
	public int col;
	
	public CFException(String mess, CFContext context) {
		super();
		
		int line = context.getLine();
		int col = context.getCol();
		
		init(mess, line, col);
	}
	
	private void init(String mess, int lineInit, int colInit) {
		line = lineInit;
		col = colInit;
	}
	
	public String toString() {
		return "[line " + String.valueOf(line) + ", column " + String.valueOf(col) + "] " + super.toString();
	}
	
}
