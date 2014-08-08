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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class sourceReader extends Object {
	
	private List<String> lineBuffer = new ArrayList<String>();
	private int lineStart, lineCount;
	
	public sourceReader( BufferedReader in ) throws IOException {
		this( in, 0, Integer.MAX_VALUE );
	}
	
	public sourceReader( BufferedReader in, int _lineStart, int _lineCount ) throws IOException {
		
		lineStart	= _lineStart;
		lineCount	= _lineCount;
		
		if ( lineStart < 0 ) lineStart = 0;
		
		//--[ Skip a head
		int currentLine = 0;
		while ( ( currentLine < lineStart ) && ( in.readLine() != null ) ) {
			currentLine++;
		}
		
		if ( currentLine != lineStart )
			throw new IOException( "sourceReader: not enough lines in the file" );
		
		String line;
		for ( int i = 0; ( i < lineCount ) && ( ( line = in.readLine() ) != null ); i++ ) {
			lineBuffer.add( line );
		}
	}
	
	public String[] getLines(){
		return lineBuffer.toArray( new String[ lineBuffer.size() ] );
	}
	
	public int getLineStart(){
		return lineStart;
	}
}
