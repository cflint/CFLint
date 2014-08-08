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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class userDefinedFunction implements java.io.Serializable {
	private static final long serialVersionUID = 1;
	
	public static final int ACCESS_PRIVATE = 0;
	public static final int ACCESS_PACKAGE = 1;
	public static final int ACCESS_PUBLIC = 2;
	public static final int ACCESS_REMOTE = 3;
	
	protected String name;
	protected int access = -1;
	private String returnType;
	
	private Map<String, String> attributes; // the function attributes
	
	// the following attribute is only used for CFFUNCTION-based UDFs
	
	// the following two attributes are only used for CFSCRIPT-based UDFs
	private CFScriptStatement body;
	protected List<CFFunctionParameter> formals; // a list of argument names in Strings
	
	// for UDFs within a CFC; needs to be per-CFC instance
	
	// for cfScript:Java
	protected JavaBlock javaBlock;
	protected Method javaMethod;
	
	// for creating CFSCRIPT-based UDFs
	public userDefinedFunction(String _name, byte _access, String _returnType, List<CFFunctionParameter> _formals,
			Map<String, String> _attr, CFScriptStatement _body) {
		name = _name;
		access = _access;
		formals = _formals;
		attributes = _attr;
		body = _body;
		returnType = _returnType;
	}
	
	public userDefinedFunction(Method method, JavaBlock javaBlock) {
		name = method.getName();
		this.javaBlock = javaBlock;
		this.javaMethod = method;
		access = ACCESS_PUBLIC;
		formals = new ArrayList<CFFunctionParameter>();
	}
	
	public String getTypeString() {
		return "UDF";
	}
	
	public List<CFFunctionParameter> getUDFFormals() {
		return formals;
	}
	
	public String getName() {
		return name;
	}
	
	public String getString() {
		return name;
	}
	
	public boolean isJavaBlock() {
		return (javaBlock != null);
	}
	
	public JavaBlock getJavaBlock() {
		return javaBlock;
	}
	
	public void setJavaBlock(JavaBlock jb) {
		javaBlock = jb;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	public void dump(PrintWriter out) {
	}
	
	public String toString() {
		StringWriter w = new StringWriter();
		this.dump(new PrintWriter(w));
		return w.toString();
	}
	
}
