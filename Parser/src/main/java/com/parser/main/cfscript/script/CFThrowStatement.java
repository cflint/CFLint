/* 
 * Copyright (C) 2000 - 2008 TagServlet Ltd
 *
 * This file is part of the BlueDragon Java Open Source Project.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.parser.main.cfscript.script;

import com.parser.main.cfscript.CFExpression;

public class CFThrowStatement extends CFParsedStatement implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private CFExpression message;
	
	public CFThrowStatement(org.antlr.runtime.Token t, CFExpression _message) {
		super(t);
		message = _message;
	}
	
	@Override
	public String Decompile(int indent) {
		if (message != null) {
			return "throw \"" + message + "\"";
		} else {
			return "throw";
		}
	}
}
