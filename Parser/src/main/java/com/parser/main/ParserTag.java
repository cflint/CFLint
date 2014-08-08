package com.parser.main;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.StartTag;

public class ParserTag {
	
	private String fName;
	private int fStartTagBegin;
	private int fStartTagEnd;
	private int fEndTagBegin;
	private int fEndTagEnd;
	private int fBegin;
	private int fEnd;
	private Attributes fAttributes;
	
	public ParserTag() {
		// TODO Auto-generated constructor stub
	}
	
	public ParserTag(String name, int startTagBegin, int startTagEnd, int endTagBegin, int endTagEnd,
			Attributes attributes) {
		setName(name);
		setBegin(startTagBegin);
		setEnd(endTagEnd);
		setStartTagBegin(startTagBegin);
		setStartTagEnd(startTagEnd);
		setEndTagBegin(endTagBegin);
		setEndTagEnd(endTagEnd);
		setAttributes(attributes);
	}
	
	public ParserTag(StartTag tag) {
		setName(tag.getName());
		setBegin(tag.getElement().getEnd());
		setEnd(tag.getElement().getBegin());
		setStartTagBegin(tag.getElement().getStartTag().getBegin());
		setStartTagEnd(tag.getElement().getStartTag().getEnd());
		if (tag.getElement().getEndTag() != null) {
			setEndTagBegin(tag.getElement().getEndTag().getBegin());
			setEndTagEnd(tag.getElement().getEndTag().getEnd());
		} else {
			setEndTagBegin(tag.getElement().getStartTag().getBegin());
			setEndTagEnd(tag.getElement().getStartTag().getEnd());
		}
		setAttributes(tag.getAttributes());
	}
	
	public ParserTag(net.htmlparser.jericho.Tag tag) {
		setName(tag.getName());
		setBegin(tag.getElement().getEnd());
		setEnd(tag.getElement().getBegin());
		setStartTagBegin(tag.getElement().getStartTag().getBegin());
		setStartTagEnd(tag.getElement().getStartTag().getEnd());
		if (tag.getElement().getEndTag() != null) {
			setEndTagBegin(tag.getElement().getEndTag().getBegin());
			setEndTagEnd(tag.getElement().getEndTag().getEnd());
		} else {
			setEndTagBegin(tag.getElement().getStartTag().getBegin());
			setEndTagEnd(tag.getElement().getStartTag().getEnd());
		}
		setAttributes(tag.getElement().getAttributes());
	}
	
	/**
	 * @param fName
	 *            the fName to set
	 */
	public void setName(String fName) {
		this.fName = fName;
	}
	
	/**
	 * @return the fName
	 */
	public String getName() {
		return fName;
	}
	
	/**
	 * @param fStart
	 *            the fStart to set
	 */
	public void setBegin(int fStart) {
		this.fBegin = fStart;
	}
	
	/**
	 * @return the fStart
	 */
	public int getBegin() {
		return fBegin;
	}
	
	/**
	 * @param fEnd
	 *            the fEnd to set
	 */
	public void setEnd(int fEnd) {
		this.fEnd = fEnd;
	}
	
	/**
	 * @return the fEnd
	 */
	public int getEnd() {
		return fEnd;
	}
	
	/**
	 * @param fAttributes
	 *            the fAttributes to set
	 */
	public void setAttributes(Attributes fAttributes) {
		this.fAttributes = fAttributes;
	}
	
	/**
	 * @return the fAttributes
	 */
	public Attributes getAttributes() {
		return fAttributes;
	}
	
	/**
	 * @param fStartTagBegin
	 *            the fStartTagBegin to set
	 */
	public void setStartTagBegin(int fStartTagBegin) {
		this.fStartTagBegin = fStartTagBegin;
	}
	
	/**
	 * @return the fStartTagBegin
	 */
	public int getStartTagBegin() {
		return fStartTagBegin;
	}
	
	/**
	 * @param fStartTagEnd
	 *            the fStartTagEnd to set
	 */
	public void setStartTagEnd(int fStartTagEnd) {
		this.fStartTagEnd = fStartTagEnd;
	}
	
	/**
	 * @return the fStartTagEnd
	 */
	public int getStartTagEnd() {
		return fStartTagEnd;
	}
	
	/**
	 * @param fEndTagBegin
	 *            the fEndTagBegin to set
	 */
	public void setEndTagBegin(int fEndTagBegin) {
		this.fEndTagBegin = fEndTagBegin;
	}
	
	/**
	 * @return the fEndTagBegin
	 */
	public int getEndTagBegin() {
		return fEndTagBegin;
	}
	
	/**
	 * @param fEndTagEnd
	 *            the fEndTagEnd to set
	 */
	public void setEndTagEnd(int fEndTagEnd) {
		this.fEndTagEnd = fEndTagEnd;
	}
	
	/**
	 * @return the fEndTagEnd
	 */
	public int getEndTagEnd() {
		return fEndTagEnd;
	}
	
}
