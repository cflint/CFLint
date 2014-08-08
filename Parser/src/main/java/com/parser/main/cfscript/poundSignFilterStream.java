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

package com.parser.main.cfscript;

/**
 * This class filters out strings containing pound signs replacing them with
 * concatenation. 
 *
 * Also removes the quotes from a string with only one 
 */

import java.io.FilterReader;
import java.io.CharArrayWriter;
import java.io.Reader;
import java.io.IOException;

public class poundSignFilterStream extends FilterReader {
	
	char[] buffer = null;
	int bufferAt;
	int nextChar;
	boolean temp = false;
	int added; // keeps a count of the adjustment in length of the input vs
	// output
	private boolean isClosed;
	
	public poundSignFilterStream(Reader _in) throws IOException {
		super(_in);
		added = 0;
		readChar();
	}
	
	synchronized public int read() throws IOException {
		// if the buffer is empty then
		if (buffer == null) {
			int returnChar;
			// if it's the start of a comment then read it into the buffer
			if (nextChar == '/') {
				readChar();
				if (nextChar == '*') {
					buffer = readComment();
					bufferAt = 0;
				} else if (nextChar == '/') {
					buffer = readLineComment();
					bufferAt = 0;
				}
				returnChar = '/'; // in all cases
				// if it's the start of a string then read the string into the
				// buffer
			} else if (nextChar == '"' || nextChar == '\'') {
				buffer = readString(true);
				bufferAt = 1;
				returnChar = buffer[0];
				if (buffer.length == 1)
					buffer = null;
			} else { // else return the next char in the stream
				returnChar = nextChar;
				readChar();
			}
			
			return returnChar;
			
		} else { /* buffer isn't empty so return next char from it */
			int bufferChar = buffer[bufferAt];
			bufferAt++;
			if (bufferAt >= buffer.length)
				buffer = null;
			
			return bufferChar;
		}
		
	}
	
	synchronized public int read(char[] cbuf, int off, int len) throws IOException {
		int offset = off;
		int actualLen = 0;
		int nextChar = -1;// = read();
		
		// note that read() is used as opposed to readChar() so that the #
		// filtering
		// is performed at that level
		
		while (actualLen < len && ((nextChar = read()) != -1)) {
			cbuf[offset] = (char) nextChar;
			actualLen++;
			offset++;
		}
		
		if (nextChar == -1 && actualLen == 0) {
			return -1;
		}
		
		return actualLen;
	}
	
	/**
	 * reads in the whole string from the input stream and does the following before putting it in the buffer: - removes
	 * escaped single and double quotes, and pound signs - replaces embedded pound sign expression within the string
	 * with a string concatentation with the function/variable
	 * 
	 * @param _withinExpression
	 *            if true then this indicates that the string is within an expression so in the case "#poundsignexpr#",
	 *            it will be reduced to poundsignexpr as opposed to #poundsignexpr# as it would do normally
	 */
	
	private char[] readString(boolean _withinExpression) throws IOException {
		CharArrayWriter cout = new CharArrayWriter(); // might want to share
		// this
		int endMarker = nextChar;
		cout.write(endMarker); // write the " or ' surrounding the string
		
		readChar();
		
		while (nextChar != -1) {
			
			// if next character is start of a pound sign expression
			if (nextChar == '#') {
				readChar(); // skip the pound sign
				// is it an escape char
				if (nextChar == '#') {
					// escape it - i.e. don't add it onto the stream
					added--;
					cout.write('#');
					readChar();
					continue;
				} else { // # followed by non-#
					cout.write(endMarker);
					cout.write('&'); // instead of the pound sign
					
					// copy these and write them to a new buffer with pound
					// signs if the
					// next char is
					// a # and the char after that is a endMarker and the one
					// after that
					// isn't an end marker
					CharArrayWriter expression = new CharArrayWriter();
					while (nextChar != '#' && nextChar != -1) {
						if (nextChar == '\'' || nextChar == '"') {
							expression.write(readString(true));
						} else {
							expression.write(nextChar);
							readChar();
						}
						
					}
					
					// now add the appropriate &" or whatever
					if (nextChar == '#') {
						readChar(); // skip it
						
						if (nextChar != endMarker) {
							expression.writeTo(cout);
							cout.write('&');
							cout.write(endMarker);
							added += 2;
						} else {
							readChar();
							
							if (nextChar != endMarker) {
								if (cout.size() == 3) {
									if (!_withinExpression) {
										cout.reset();
										cout.write('#');
										expression.writeTo(cout);
										cout.write('#');
										added++;
										return cout.toCharArray();
										
									} else { // nextChar != endMarker &&
										// cout.size() == 3 &&
										// withinExpression
										cout.reset();
										expression.writeTo(cout);
										added -= 4;
										return cout.toCharArray();
									}
								} else { // nextChar != endMarker
								
									// end of string found
									added--;
									expression.writeTo(cout);
									return cout.toCharArray();
								}
							} else {
								expression.writeTo(cout);
								cout.write('&');
								cout.write(endMarker);
								cout.write(endMarker); // want to write the
								// escaped end marker
								// here
								cout.write(endMarker); // else won't be escaped
								// properly if
								// allow it to pass thru
								added += 2;
								readChar();
							}
						}
					} else {
						// throw an exception
						throw new poundSignFilterStreamException("Invalid string expression - unclosed '#' expression");
					}
					
				}
				
				// else if reached endmarker - could be quote or double quote
			} else if (nextChar == endMarker) {
				readChar();
				// is it an escape char; if so carry on
				if (nextChar == endMarker) {
					cout.write(endMarker);
					cout.write(endMarker);
					readChar();
				} else { // else return the read string
					cout.write(endMarker);
					
					return cout.toCharArray();
				}
				// else, just a regular char; add it to the string being read
			} else {
				cout.write(nextChar);
				readChar();
			}
		}
		
		throw new IOException("Unclosed string expression - missing " + endMarker + ".");
	}
	
	public void close() throws IOException {
		super.close();
		this.isClosed = true;
	}
	
	private int readChar() throws IOException {
		if (this.isClosed) {
			return -1;
		}
		nextChar = in.read();
		return nextChar;
	}
	
	private char[] readComment() throws IOException {
		CharArrayWriter cout = new CharArrayWriter();
		boolean endComment = false;
		cout.write('*');
		readChar(); // read in the '*'
		while (!endComment && nextChar != -1) {
			cout.write(nextChar);
			if (nextChar == '*') {
				readChar();
				if (nextChar == '/') {
					cout.write('/');
					readChar();
					endComment = true;
				}
			} else {
				readChar();
			}
		}
		return cout.toCharArray();
	}
	
	private char[] readLineComment() throws IOException {
		CharArrayWriter cout = new CharArrayWriter();
		while (nextChar != '\r' && nextChar != '\n' && nextChar != -1) {
			cout.write(nextChar);
			readChar();
		}
		return cout.toCharArray();
	}
	
	public int getAdded() {
		return added;
	}
	
	public static void main(String[] args) {
		java.util.ArrayList inputList = new java.util.ArrayList();
		
		inputList.add("writeoutput( \"##\" );");
		inputList.add("writeoutput( \"#a##b#\" );");
		inputList.add("writeoutput( \"#a#\" );");
		inputList.add("writeoutput( \"#a#more text\" );");
		inputList.add("writeoutput( \"more text#a#\" );");
		inputList.add("writeoutput( \"more text#a#more text\" );");
		inputList.add("writeoutput( \"more#a#text#a#more#a#text\" );");
		inputList.add("x = 1;\nwriteoutput( \"#x#\" );");
		inputList.add("/* this is a comment line */\na = 1;\nwriteoutput( a );");
		inputList.add("/* this is a comment line */\na = 1;\nwriteoutput( a );");
		StringBuffer lastTest = new StringBuffer();
		for (int i = 0; i < inputList.size(); i++) {
			lastTest.append((String) inputList.get(i));
			lastTest.append("\r\n");
		}
		inputList.add(lastTest.toString());
		inputList.add("/*aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				+ "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				+ "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				+ "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				+ "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa*/");
		
		for (int i = 0; i < inputList.size(); i++) {
			String input = (String) inputList.get(i);
			try {
				poundSignFilterStream psf = new poundSignFilterStream(new java.io.StringReader(input));
				char[] buffer = new char[1024];
				int read = psf.read(buffer);
				System.out.println("BEFORE: " + input.length() + "; AFTER: " + read + "; Added: " + psf.added
						+ "; Total = " + (read - psf.added));
				System.out.println(input + " => " + new String(buffer, 0, read));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
