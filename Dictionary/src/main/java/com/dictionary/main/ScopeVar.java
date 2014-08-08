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

/**
 * @author Rob
 *
 * This is really just a string wrapper. Its a possible value for an attribute
 * its only here to abstract it a bit and to give some room to play for future 
 * additions
 */
public class ScopeVar implements Comparable {
	protected String type;
	protected String name;
	protected String help;
	public ScopeVar(String type, String val)
	{
		type = type;
		name = val;
		this.help = "";	// Must set this to something even if there is no help
	}
	
	/**
	 * gets this values value
	 * @return the value
	 */
	public String getName()
	{
		return name.trim();
	}
	
	public void setName(String newValue)
	{
		name = newValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getHelp() {
	    return help;
	}
	
	public void setHelp(String help) {
	    this.help = help;
	}
	
	public String toString()
	{
		return name;
	}

	/**
	 * Checks to see if the passed object is equal to this
	 * value
	 */
	public boolean equals(Object obj)
	{
		if(obj instanceof ScopeVar)
		{
			//if the name is the same and the type is the same
			//assume its the same
			if( ((ScopeVar)obj).getName().equals(this.name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Compares the passed object to this value
	 */
	public int compareTo(Object o)
	{
		if(o == null)
			throw new NullPointerException("Null!");
		
		if(o instanceof ScopeVar)
		{
			return name.compareTo( ((ScopeVar)o).getName() );
		}
		
		return 0;
	}
	
}
