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
 * <p>
 * This class represents a syntax dictionary tag (should probably be renamed to DictionaryTag or something similar).
 * Like Function it inherits from Procedure as a Tag is really a Procedure - it takes parameters and returns no value.
 * Bear that in mind :)
 * </p>
 * <p>
 * A tag has a number of states with regards to it's HTML/XML compatibility. For example:
 * </p>
 * <ul>
 * <li>Single: The tag is something like a &lt;cfbreak&gt;</li>
 * <li>xmlstyle: The tag is XML compatible like a &lt;cfbreak/&gt;</li>
 * <li>hybrid: ????</li>
 * </ul>
 * 
 * 
 * @author Rob
 * 
 */
public class Tag extends Procedure {
	/** Is this tag a single tag or not? */
	protected boolean single = false;
	/** Is this tag xmlstyle? (i.e. &lt;br/&gt;) */
	protected boolean xmlstyle = false;
	/** Is this tag a hybrid tag (???) */
	protected boolean hybrid = false;
	/** Can this tag take any attribute like a &lt;cfif&gt; */
	protected boolean anyAttribute = false;
	
	/** w3c form tag */
	public static final byte FORM = 16;
	/** w3c table tag */
	public static final byte TABLE = 32;
	
	/**
	 * Constructs a new Tag with the specified name and whether or not it is a single tag or not (i.e. &lt;cfbreak&gt;).
	 * 
	 * @param name
	 *            The name of the tag
	 * @param single
	 *            Is the tag a single tag or not
	 */
	public Tag(String name, boolean single) {
		super(name);
		this.single = single;
	}
	
	/**
	 * Creates a tag with a series of tag-based info. The name, whether or not the tag is single or not, the style (is
	 * it xmlstyle or not) and it's creator (see syntax dictionaries for more info).
	 * 
	 * @param name
	 *            The name of the tag
	 * @param single
	 *            Is the tag a single tag or not?
	 * @param style
	 *            Is the tag XML style or not?
	 * @param creator
	 *            The creator/author of the tag
	 */
	public Tag(String name, boolean single, boolean style, byte creator) {
		this(name, single);
		this.creator = creator;
		xmlstyle = style;
	}
	
	/**
	 * Creates a tag with a series of tag-based info. The name, whether or not the tag is single or not, the style (is
	 * it xmlstyle or not) and it's creator (see syntax dictionaries for more info). Additionally it takes whether or
	 * not the tag is a hybrid (???) and whether it can take any attribute (ala &lt;cfif&gt;).
	 * 
	 * @param name
	 *            The name of the tag
	 * @param single
	 *            Is the tag a single tag or not?
	 * @param style
	 *            Is the tag XML style or not?
	 * @param creator
	 *            The creator/author of the tag
	 * @param hybrid
	 *            Is the tag a hybrid or not
	 * @param anyAttribute
	 *            Can the tag take any attribute
	 */
	public Tag(String name, boolean single, boolean style, byte creator, boolean hybrid, boolean anyAttribute) {
		this(name, single);
		this.creator = creator;
		xmlstyle = style;
		this.hybrid = hybrid;
		this.anyAttribute = anyAttribute;
	}
	
	/**
	 * returns true if this is a table tag
	 * 
	 * @return
	 */
	public boolean isTableTag() {
		if (getCreatorFlags() == TABLE) {
			return true;
		}
		return false;
	}
	
	/**
	 * returns true if this is a form tag
	 * 
	 * @return
	 */
	public boolean isFormTag() {
		if (getCreatorFlags() == FORM) {
			return true;
		}
		return false;
	}
	
	/**
	 * Is this tag a sinlge tag or does it have a closing counter part?
	 * 
	 * @return if single or not
	 */
	public boolean isSingle() {
		return single;
	}
	
	/**
	 * Is this tag in xml style (mostly used with is single to tell if the tag shoule be &lt;tag&gt; or &lt;tag/&gt;
	 * 
	 * @return if xml style or not
	 */
	public boolean isXMLStyle() {
		return xmlstyle;
	}
	
	/**
	 * Is this tag a hybrid that can be either single or paired: e.g. &lt;cftransaction&gt; and &lt;cfinvoke&gt;
	 */
	public boolean isHybrid() {
		return hybrid;
	}
	
	/**
	 * Does the tag allow any attribute: e.g. &lt;cfmodule&gt;
	 */
	public boolean allowsAnyAttribute() {
		return anyAttribute;
	}
	
	/**
	 * Is this tag a custom tag
	 * 
	 * @return
	 */
	
	public boolean isCustomTag() {
		boolean iscustom = false;
		
		if (this.name.toLowerCase().startsWith("cf_") || this.name.toLowerCase().startsWith("cfx_")) {
			iscustom = true;
		}
		return iscustom;
	}
	
	public String toString() {
		return name;
	}
}
