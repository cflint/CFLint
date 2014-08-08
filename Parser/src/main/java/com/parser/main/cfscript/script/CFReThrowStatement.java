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

public class CFReThrowStatement extends CFParsedStatement implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public CFReThrowStatement(org.antlr.runtime.Token t) {
		super(t);
	}
	
	@Override
	public String Decompile(int indent) {
		return "rethrow";
	}
}
