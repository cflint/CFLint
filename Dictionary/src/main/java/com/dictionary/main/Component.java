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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Spike
 * 
 */
public class Component extends Procedure {
	protected String path = "";
	protected String framework = "";
	protected Set methods = null;
	protected Set scopes = null;
	
	public Component(String name, String path, String framework, byte creator) {
		super(name);
		this.path = path;
		this.framework = framework;
		this.creator = creator;
	}
	
	public void addMethod(Function method) {
		if (methods == null)
			methods = new LinkedHashSet();
		
		methods.add(method);
	}
	
	public void addScope(String scope) {
		if (scopes == null)
			scopes = new LinkedHashSet();
		
		scopes.add(scope);
	}
	
	public String toString() {
		return name;
	}
	
	public Set getScopes() {
		return this.scopes;
	}
	
	public Set getMethods() {
		return this.methods;
	}
	
}
