/*
 * Created on Feb 27, 2004
 *
 * The MIT License
 * Copyright (c) 2004 Rob Rohan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software 
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package com.dictionary.main;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * This is a function. Functions are like tags except they have a return type. A function derives from the Procedure
 * class but as mentioned a moment ago, it has a return value.
 * 
 * @author Rob
 * 
 */
public class Function extends Procedure {
	
	/** return type */
	protected String returns = Procedure.VOID;
	
	/**
	 * creates a function with only a name
	 * 
	 * @param name
	 *            the attribute name
	 */
	public Function(String name) {
		super(name);
	}
	
	public Function(String name, byte creator) {
		super(name);
		this.creator = creator;
	}
	
	/**
	 * Function needs to override because param order is important (makes parameters a LinkedHashSet)
	 */
	public void addParameter(Parameter param) {
		if (parameters == null)
			parameters = new LinkedHashSet();
		
		parameters.add(param);
	}
	
	/**
	 * creates a function with a name and a type
	 * 
	 * @param name
	 *            the name
	 * @param type
	 *            the type typically string numeric or object
	 */
	public Function(String name, String returntype, byte creator) {
		this(name, creator);
		this.returns = returntype;
	}
	
	/**
	 * override toString to auto format the function
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this.name != null) {
			// to lowercase for the createobject hack
			sb.append(this.name + "(");
			
			if (parameters != null) {
				Iterator it = parameters.iterator();
				while (it.hasNext()) {
					Parameter pm = (Parameter) it.next();
					if (!pm.isRequired())
						sb.append("[");
					sb.append(pm.getType() + " " + pm.getName());
					if (!pm.isRequired())
						sb.append("]");
					sb.append(", ");
				}
				sb.delete(sb.length() - 2, sb.length());
			}
			sb.append(")");
			sb.append(" : " + this.returns);
		}
		
		return sb.toString();
	}
	
	/**
	 * override toString to auto format the function
	 */
	public String getInsertion() {
		StringBuffer sb = new StringBuffer();
		if (this.name != null) {
			// sb.append(this.returns + " ");
			// to lowercase for the createobject hack
			sb.append(this.name + "(");
			
			if (parameters != null) {
				Iterator it = parameters.iterator();
				while (it.hasNext()) {
					Parameter pm = (Parameter) it.next();
					if (!pm.isRequired())
						sb.append("[");
					sb.append(pm.getType() + " " + pm.getName());
					if (!pm.isRequired())
						sb.append("]");
					sb.append(", ");
				}
				sb.delete(sb.length() - 2, sb.length());
			}
			sb.append(")");
		}
		
		return sb.toString();
	}
}
