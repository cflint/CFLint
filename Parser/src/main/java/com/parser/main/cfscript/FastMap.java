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

import java.io.Serializable;

import javolution.util.FastComparator;

/**
 * A subclass of javolution.util.FastMap that supports case-insensitive keys. Keys are always strings.
 * 
 * FastMap performs better than java.util.HashMap, and also gives us the ability to provide a key comparator, which is
 * how case-insensitivity is implemented.
 */
public class FastMap<K, V> extends javolution.util.FastMap<String, V> implements CaseSensitiveMap<String, V>,
		Serializable, Cloneable {
	static final long serialVersionUID = 1;
	
	public static final boolean CASE_SENSITIVE = true;
	
	public static final boolean CASE_INSENSITIVE = false;
	
	// use the FastMap default key comparator for case-sensitive comparisons
	public static FastComparator<String> stringComparatorIgnoreCase = new StringComparatorIgnoreCase();
	
	public FastMap() { // FastMaps are case-sensitive by default
	}
	
	public FastMap(FastMap<String, V> map) {
		setKeyComparator(map.getKeyComparator()); // must be done before
		// putAll()
		setShared(map.isShared());
		putAll(map);
	}
	
	public FastMap(java.util.Map<String, V> map) {
		putAll(map);
	}
	
	public FastMap(int initialCapacity) {
		super(initialCapacity);
	}
	
	// this constructor is not part of the standard java.util.Map interface
	public FastMap(boolean isCaseSensitive) {
		if (!isCaseSensitive) {
			setKeyComparator(stringComparatorIgnoreCase);
		}
	}
	
	public Object clone() {
		return new FastMap<String, V>(this);
	}
	
	// this method is not part of the standard java.util.Map interface
	public boolean isCaseSensitive() {
		return (getKeyComparator() != stringComparatorIgnoreCase);
	}
	
	// case-insensitive string comparator for use by FastMap
	private static class StringComparatorIgnoreCase extends FastComparator<String> {
		private static final long serialVersionUID = 1L;
		
		// the formal definition of this method says you're supposed to allow
		// nulls;
		// but we're dealing with keys here, which aren't allowed to be null, so
		// don't
		// bother checking for nulls
		public boolean areEqual(String key1, String key2) {
			return key1.equalsIgnoreCase(key2);
		}
		
		public int compare(String key1, String key2) {
			return key1.compareToIgnoreCase(key2);
		}
		
		public int hashCodeOf(String key) {
			// toLowerCase() performs much better here than toUpperCase()
			return key.toLowerCase().hashCode();
		}
	}
}
