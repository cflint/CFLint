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

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author Rob
 *
 * Handles the Sax events against a dictionary. This was ripped from Treebeard
 * with permission
 */
public class DictionaryContentHandler implements ContentHandler {
	//keep track of the location
	//private Locator locator;
	//private Map namespaceMappings;
	
	private Map dtags;
	private Map dfunctions;	
	private Map dscopeVars;
	private Map dscopes;

	/** used to mark which part of the xml doc we are in */
	private String currenttag = "";
	/** current tag/function being built */
	private Procedure currentitem = null;
	private Parameter paramItem = null;
	private Function methoditem = null;
	
	public DictionaryContentHandler(Map tags, Map functions, Map scopeVars, Map scopes)
	{
		dtags = tags;
		dfunctions = functions;
		dscopeVars = scopeVars;
		dscopes = scopes;
		//namespaceMappings = new java.util.HashMap();
	}
	
	/** sets the documnet locator
	 * @param locator the document locator
	 */	
	public void setDocumentLocator(Locator locator) 
	{
		//this.locator = locator;
	}
	
	/**
	 * guesses if the passed string is "true" or "false"
	 * @param bstring the "true" "false" string
	 * @return the string in a boolean form
	 */
	private boolean parseBoolean(String bstring)
	{
		if(bstring.equalsIgnoreCase("true") || bstring.equalsIgnoreCase("yes"))
		{
			return true;
		}
		
		return false;
	}
	
	private boolean inTriggerBlock = false;
	
	/**
	 * Handles the start of a triggers tag.
	 * At present it just records the fact that we are in a triggers block
	 * so that any trigger set exists only in triggers blocks.
	 * 
	 * @param attribs XML tag attributes
	 */
	private void handleTriggersStart(Attributes attribs)  {
		if(this.paramItem == null) {
			System.err.println("Got a <triggers> block outside of a parameter!");
		}
		else
			this.inTriggerBlock = true;
	}
	
	/**
	 * Handles a closing triggers tag.
	 * 
	 * Simply records the fact that the triggers block has ended.
	 *
	 */
	private void handleTriggersEnd() {
		this.inTriggerBlock = false;
	}
	
	/**
	 * Handles a selectedValue tag. 
	 * Adds the trigger details to the currently open parameter (if no parameter open
	 * or the selectedValue tag is not in a parameter then an error will be reported)
	 * 
	 * @param attributes Attributes for the xml 
	 */
	private void handleSelectedValue(org.xml.sax.Attributes attributes) {
		if(!this.inTriggerBlock) {
			System.err.println("Got a <selectedValue> outside of a valid triggers block!");
			return;
		}
		
		String attrName = attributes.getValue("attributeName");
		String value = attributes.getValue("value");
		boolean required = attributes.getValue("required").compareToIgnoreCase("true") == 0;
		String indexVal = attributes.getValue("index");
		int index = -1;
		if (indexVal != null) {
			index = Integer.parseInt(indexVal);
		}
		this.paramItem.addTrigger(Trigger.CreateSimpleTrigger(attrName, value, required,index));
		
// System.out.println("DictionaryContentHandler::handleSelectedVaule - Attr: \'" + this.paramItem.getName() + "\' + Added trigger for \'" + attrName + "\'=\'" + value + "\'");
	}
	
	/** process a start element */
	public void startElement(String namespace, String localName, String str2, org.xml.sax.Attributes attributes) 
		throws SAXException 
	{
		//save the current tag so we can see where we were
		this.currenttag = str2.toLowerCase();
				
		if(str2.equals("tag")) {
			handleTagStart(attributes);
		} else if(str2.equals("function")) {
			handleFunctionStart(attributes);
		} else if(str2.equals("parameter")) {
			handleParameterStart(attributes);
		} else if(str2.equals("value")) {
			handleValueStart(attributes);
		} else if (str2.equals("help"))	{
			//adds help
		} else if(str2.equals("component"))	{
		    handleComponentStart(attributes);
//  these came with the dict from CFB side, looks like a work in progress
//		} else if(str2.equals("scopevar"))	{
//		    handleScopeStart(attributes);
		} else if(str2.equals("scope"))	{
		    handleScopeStart(attributes);
		} else if(str2.equals("triggers")) {
			handleTriggersStart(attributes);
		} else if(str2.equals("selectedValue")) {
			handleSelectedValue(attributes);
		}
		
	}
	
	
	private void handleTagStart(org.xml.sax.Attributes attributes) {
	    //all the attribtues we are going to need to make a tag
		byte creator = 0;
		String name = "";
		boolean single = false;
		boolean xmlstyle = false;
		boolean hybrid = false;
		boolean anyAttribute = false;
		
		//get all the attributes needed for the tag
		for(int x=0; x< attributes.getLength(); x++)
		{
			String attrname = attributes.getQName(x).toLowerCase();
			if(attrname.equals("creator"))
			{
				creator = Byte.parseByte(attributes.getValue(x));
			}
			else if(attrname.equals("name"))
			{
				name = attributes.getValue(x);
			}
			else if(attrname.equals("single"))
			{
				single = parseBoolean(attributes.getValue(x));
			}
			else if(attrname.equals("xmlstyle"))
			{
				xmlstyle = parseBoolean(attributes.getValue(x));
			}
			else if(attrname.equals("hybrid"))
			{
				hybrid = parseBoolean(attributes.getValue(x));
			}
			else if(attrname.equals("allowanyattribute"))
			{
				anyAttribute = parseBoolean(attributes.getValue(x));
			}
		}
		
		//System.out.println("Tag: " + creator + " " + name + " " + single + " " + xmlstyle);
		//create a new tag
		this.currentitem = new Tag(name,single,xmlstyle,creator,hybrid,anyAttribute);
	}
	
	
	
	private void handleFunctionStart(org.xml.sax.Attributes attributes) {
	    //create a new function
		byte creator = 0;
		String name = "";
		String returns = "";
		
		//get all the attributes needed for the tag
		for(int x=0; x< attributes.getLength(); x++)
		{
			String attrname = attributes.getQName(x).toLowerCase();
			if(attrname.equals("creator"))
			{
				creator = Byte.parseByte(attributes.getValue(x));
			}
			else if(attrname.equals("name"))
			{
				name = attributes.getValue(x);
			}
			else if(attrname.equals("returns"))
			{
				returns = attributes.getValue(x);
			}
		}
		//System.out.println("Fun: " + creator + " " + name + " " + returns);
		//create a new function
		if (this.currentitem instanceof Component) 
		{
		    this.methoditem = new Function(name, returns, creator);
		}
		else
		{
		    this.currentitem = new Function(name, returns, creator);
		}
	}
	
	/**
	 * Handles a new parameter.
	 * Not added until the closing tag is found.
	 * 
	 * @param attributes The attributes associated with the parameter
	 */
	private void handleParameterStart(org.xml.sax.Attributes attributes) {
	    //get the name and type
		String name = "";
		String type = "";
		boolean required = false;
		String defaultValue = null;
		String category = "General";
		
		for(int x=0; x< attributes.getLength(); x++)
		{
			String attrname = attributes.getQName(x).toLowerCase();
			if(attrname.equals("type"))	{
				type = attributes.getValue(x);
			}
			else if(attrname.equals("name")) {
				name = attributes.getValue(x);
			}
			else if(attrname.equals("required")) {
				required = parseBoolean(attributes.getValue(x));
			}
			else if(attrname.equals("default")) {
				defaultValue = attributes.getValue(x);
			} 
			else if(attrname.equals("category")){
				category = attributes.getValue(x);
			}
		}
		
		//System.out.println("Param: " + name + " " + type + " " + required);
		//
		// Create a new parameter and store it as the current parameter
		this.paramItem = new Parameter(name,type,required,defaultValue, category);
	}
	
	/**
	 * Handles a closing parameter tag. 
	 * Performs whatever finishing up is required and stores the
	 * parameter for it's associated method/tag. 
	 */
	private void handleParameterEnd() {
		//
		// Attach the finished parameter to the current item
		if((currentitem instanceof Function || currentitem instanceof Tag )
			&& paramItem != null)
		{
			currentitem.addParameter(paramItem);
		}	// TODO: Isn't the below in the wrong place, should it be a child of the above if?
		else if (methoditem != null && paramItem != null) {
		    methoditem.addParameter(paramItem);
		}
		
		//
		// Reset the paramitem
		paramItem = null;
	}	
	
	/**
	 * Handles the open tag for a value.
	 * 
	 * 
	 * @param attributes The attributes for the tag.
	 */
	private void handleValueStart(org.xml.sax.Attributes attributes) {
		//
	    // Create a new value and assign it to the current parameter
		String option = attributes.getValue(0);

		if(option != null && paramItem != null)
			paramItem.addValue(new Value(option));
	}
	
	private void handleValueEnd() {
		
	}
	
	private void handleComponentStart(org.xml.sax.Attributes attributes) {
//	  all the attribtues we are going to need to make a tag
		byte creator = 0;
		String path = "";
		String name = "";
		String framework = "";;
		
		//get all the attributes needed for the tag
		for(int x=0; x< attributes.getLength(); x++)
		{
			String attrname = attributes.getQName(x).toLowerCase();
			if(attrname.equals("creator"))
			{
				creator = Byte.parseByte(attributes.getValue(x));
			}
			else if(attrname.equals("path"))
			{
				path = attributes.getValue(x);
				String[] tmp = path.split("\\.");
				name = tmp[tmp.length-1];
			}
			else if(attrname.equals("framework"))
			{
				framework = attributes.getValue(x);
			}
		}
		
		//System.out.println("Tag: " + creator + " " + name + " " + single + " " + xmlstyle);
		//create a new tag
		this.currentitem = new Component(name,path,framework,creator);
	}
	

	
	
	private void handleScopeStart(org.xml.sax.Attributes attributes) {
	    //create a new value and assign it to the current parameter
		String type = attributes.getValue(0);
		String scopeVar = attributes.getValue(1);
		if (scopeVar.indexOf(".") > 0) {
			String scope = scopeVar.substring(0, scopeVar.indexOf("."));
			dscopes.put(scope, new ScopeVar(type, scope));
		}
		//System.out.println("Value: " + scope);
		if(currentitem instanceof Component) {
			((Component)currentitem).addScope(scopeVar);
			dscopeVars.put(scopeVar,currentitem);
		}
		else {
		    dscopeVars.put(scopeVar,new ScopeVar(type,scopeVar));
		}
	}
	
	
	
	
	/** process an end element */
	public void endElement(String str, String str1, String str2) throws SAXException {
	    str2 = str2.toLowerCase();
		if(str2.equals("tag"))
		{
			//add the current item to the tag map
			if(this.currentitem != null)
				dtags.put(currentitem.getName(),(Tag)currentitem);
			//System.err.println(this.currentitem);
		}
		else if(str2.equals("function"))
		{
			//add the current item to the function map
			if(this.currentitem instanceof Component)
			{
				((Component)currentitem).addMethod(methoditem);
				methoditem = null;
			}
			else if (this.currentitem instanceof Function) {
			    dfunctions.put(currentitem.getName(),(Function)currentitem);
			}
		}
		else if(str2.equals("parameter"))
		{
			handleParameterEnd();
		}
		else if(str2.equals("value"))
		{
			handleValueEnd();
		}
		else if(str2.equals("help"))
		{
			//nothing?
		}
		else if(str2.equals("component"))
		{
		    if (this.currentitem instanceof Component) 
		    {
		        this.currentitem = null;
		    }
		   
		} else if(str2.equals("scope"))	{
		    // Do nothing.
		} else if(str2.equals("triggers")) {
			handleTriggersEnd();
		}
		currenttag = "";
	}

	/** process the start prefix */	
	public void startPrefixMapping(String str, String str1) throws SAXException {
		//save the mappings for later use
		//namespaceMappings.put(str1,str);
	}
	
	
	/** process the end prefix */
	public void endPrefixMapping(String str) throws SAXException {;}
		
	/** process characters */
	public void characters(char[] values, int start, int length) throws SAXException {
			
		if(currenttag.equalsIgnoreCase("help"))
		{
			StringBuffer resvalue = new StringBuffer();
			
			for(int x=start; x<(start+length); x++){
				resvalue.append(values[x]);
			}
			
			//if the current item is not null and the prams are its help for the
			//current item
			if(currentitem != null && paramItem == null)
			{
				//for some reason M8 calls this a bunch of times, but only with
				//the cfml dictionary. So this is kind of a hack to get it to
				//load the help right... this slows it down a bit
				//TODO figure out whats up
				if(resvalue.toString().trim().length() > 0)
				{
					currentitem.setHelp(
						currentitem.getHelp() + " " +
						resvalue.toString().trim().replace('\t',' ') + "\n"
					);
				}
			}
			//if the param is not null its help for the param
			else if(currentitem != null && paramItem != null)
			{
				//TODO here too
				//paramitem.setHelp(resvalue.toString().trim().replace('\t',' '));
				if(resvalue.toString().trim().length() > 0)
				{
					paramItem.setHelp(
						paramItem.getHelp() + " " +
						resvalue.toString().trim().replace('\t',' ') + "\n"
					);
				}
			}
		}
	}
	
	/** process the end of the document */
	public void endDocument() throws SAXException {;}
	public void ignorableWhitespace(char[] values, int param, int param2) throws SAXException {;}
	/** process the processing instructions */
	public void processingInstruction(String str, String str1) throws SAXException {;}
	public void skippedEntity(String str) throws SAXException {;}
	/** process the start of the documnet */
	public void startDocument() throws SAXException {;}
}
