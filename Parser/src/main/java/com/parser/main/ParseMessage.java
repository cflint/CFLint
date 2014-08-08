/*
 * Created on Mar 23, 2004
 *
 * The MIT License
 * Copyright (c) 2004 Oliver Tupman
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
package com.parser.main;

/**
 * A message to be reported to the user. This is intended to be the base class for all other messages that are reported
 * to the user. Clients may create instances of this class. It's not an abstract class.
 * 
 * @author Oliver
 */
public class ParseMessage {
	/**
	 * The line number that the message should be reported for.
	 */
	protected int lineNumber;
	/**
	 * The start offset within the document that the message should be reported for.
	 */
	protected int docStartOffset;
	/**
	 * The end offset within the document that the message should be reported for.
	 */
	protected int docEndOffset;
	/**
	 * The data to be reported. Generally the relevant data from the document.
	 */
	protected String docData;
	/**
	 * The message to report to the user.
	 */
	protected String message;
	/**
	 * Was the message fatal or not (should really be in ParseError!)
	 */
	protected boolean fatal = false;
	
	/**
	 * Constructs a message that begins on a line number, starts at an offset within a document, ends at another offset
	 * with a certain data (generally docEnd - docStart substring within the document) and a message for the user.
	 * 
	 * @param lineNum
	 *            The line number
	 * @param docStart
	 *            Start offset within the doc
	 * @param docEnd
	 *            End offset within the doc
	 * @param data
	 *            Data that the message is about
	 * @param msg
	 *            The message for the user
	 */
	public ParseMessage(int lineNum, int docStart, int docEnd, String data, String msg) {
		lineNumber = lineNum;
		docStartOffset = docStart;
		docEnd = docStartOffset;
		docData = data;
		message = msg;
	}
	
	/**
	 * Constructs a message that begins on a line number, starts at an offset within a document, ends at another offset
	 * with a certain data (generally docEnd - docStart substring within the document) and a message for the user.
	 * 
	 * 
	 * @param lineNum
	 *            The line number
	 * @param docStart
	 *            Start offset within the doc
	 * @param docEnd
	 *            End offset within the doc
	 * @param data
	 *            Data that the message is about
	 * @param msg
	 *            The message for the user
	 * @param isFatal
	 *            Is the message being reported a fatal one?
	 */
	public ParseMessage(int lineNum, int docStart, int docEnd, String data, String msg, boolean isFatal) {
		lineNumber = lineNum;
		docStartOffset = docStart;
		docEnd = docStartOffset;
		docData = data;
		message = msg;
		fatal = isFatal;
	}
	
	/**
	 * Is this message fatal? If so, it generally stops the parsing of the document
	 * 
	 * @return True - this is a fatal problem, false otherwise
	 */
	public boolean isFatal() {
		return fatal;
	}
	
	/**
	 * @return Returns the docData.
	 */
	public String getDocData() {
		return docData;
	}
	
	/**
	 * @param docData
	 *            The docData to set.
	 */
	public void setDocData(String docData) {
		this.docData = docData;
	}
	
	/**
	 * @return Returns the docEndOffset.
	 */
	public int getDocEndOffset() {
		return docEndOffset;
	}
	
	/**
	 * @param docEndOffset
	 *            The docEndOffset to set.
	 */
	public void setDocEndOffset(int docEndOffset) {
		this.docEndOffset = docEndOffset;
	}
	
	/**
	 * @return Returns the docStartOffset.
	 */
	public int getDocStartOffset() {
		return docStartOffset;
	}
	
	/**
	 * @param docStartOffset
	 *            The docStartOffset to set.
	 */
	public void setDocStartOffset(int docStartOffset) {
		this.docStartOffset = docStartOffset;
	}
	
	/**
	 * @return Returns the lineNumber.
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	
	/**
	 * @param lineNumber
	 *            The lineNumber to set.
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @param message
	 *            The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		return "Line: " + lineNumber + " offset:" + docStartOffset + " endoffset:" + docEndOffset + " message:"
				+ message;
	}
}
