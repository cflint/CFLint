/* 
 *  Copyright (C) 2000 - 2008 TagServlet Ltd
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

public class cfmAbortException extends RuntimeException {
	private static final long serialVersionUID = 1;
	
	private String output;
	private boolean bFlushOutput;
	
	public cfmAbortException() {
		this(false);
	}
	
	public cfmAbortException(boolean _flushOutput) {
		bFlushOutput = _flushOutput;
	}
	
	public String getOutput() {
		return (output == null ? "" : output);
	}
	
	public boolean flushOutput() {
		return bFlushOutput;
	}
	
	// only save the output from the first caller to this method
	public void setOutput(String _output) {
		if (output == null) {
			output = _output;
		}
	}
	
	/**
	 * Overrides java.lang.Throwable. We don't care about Java stack traces for CFML runtime exceptions, so save the
	 * effort of filling it in.
	 */
	public Throwable fillInStackTrace() {
		return this;
	}
}
