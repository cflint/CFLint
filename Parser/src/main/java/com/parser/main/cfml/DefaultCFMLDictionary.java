package com.parser.main.cfml;

/*
Copyright (c) 2007 Mark Mandel, Mark Drew

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.	
*/

/**
 * @author mark
 *
 */
public class DefaultCFMLDictionary implements ICFMLDictionary
{
	public boolean conatinsCFScript(String tagName)
	{
		if(tagName.toLowerCase().equals("cfscript"))
		{
			return true;
		}
		
		return false;
	}
	
	
	public boolean isColdFusionTag(String tagName)
	{
		boolean isColdFusion = tagName.toLowerCase().startsWith("cf"); 
		
		System.out.println("isColdFusionTag: " + tagName + ":" + isColdFusion);
		
		return isColdFusion;
	}

	public boolean usesAttributes(String tagName)
	{
		tagName = tagName.toLowerCase();
		boolean usesAttributes = !(tagName.equals("cfset") || tagName.equals("cfif") || tagName.equals("cfelseif")); 
		
		System.out.println("usesAttributes: " + tagName + ":" + usesAttributes);
		
		return usesAttributes;
	}

	public boolean allowsCFMLAssignment(String tagName)
	{
		boolean assignment = tagName.toLowerCase().equals("cfset");
		
		System.out.println("allowsCFMLAssignment: " + tagName + ":" + assignment);
		
		return assignment;
	}

	public boolean allowsCFMLCondition(String tagName)
	{
		boolean condition = tagName.equals("cfif") || tagName.equals("cfelseif") || tagName.equals("cfreturn");
		
		System.out.println("allowsCFMLCondition: " + tagName + ":" + condition);
		
		return condition;
	}
}
