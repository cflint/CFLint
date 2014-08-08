// Jericho HTML Parser - Java based library for analysing and manipulating HTML
// Version 3.1
// Copyright (C) 2004-2009 Martin Jericho
// http://jericho.htmlparser.net/
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of either one of the following licences:
//
// 1. The Eclipse Public License (EPL) version 1.0,
// included in this distribution in the file licence-epl-1.0.html
// or available at http://www.eclipse.org/legal/epl-v10.html
//
// 2. The GNU Lesser General Public License (LGPL) version 2.1 or later,
// included in this distribution in the file licence-lgpl-2.1.txt
// or available at http://www.gnu.org/licenses/lgpl.txt
//
// This library is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the individual licence texts for more details.

package com.parser.main.util;

import java.util.ArrayList;

import net.htmlparser.jericho.EndTagType;
import net.htmlparser.jericho.ParseText;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagTypeGenericImplementation;
import net.htmlparser.jericho.Tag;

public class ParseUtil {
	
	protected ParseUtil() {
	}
	
	protected ArrayList getAttributes(String inData) {
		ArrayList attributes = new ArrayList();
		boolean isStartedString = false;
		boolean isStartedAttribute = false;
		boolean isDone = false;
		boolean isInQuotes = false;
		boolean isInApos = false;
		String attributeName = "";
		String attributeValue = "";
		int pos = 0;
		for (int x = pos; x < inData.length(); x++) {
			char c = inData.charAt(x);
			char nextChar;
			if (inData.length() > x + 1) {
				nextChar = inData.charAt(x + 1);
			} else {
				nextChar = '>';
			}
			switch (c) {
			case '>':
				if (!isInQuotes && !isInApos) {
					isDone = true;
				}
				break;
			case '"':
				if (!isInApos && nextChar != '"') {
					isInQuotes = (!isInQuotes);
					if (isStartedString) {
						isDone = true;
					} else {
						isStartedString = !isStartedString;
					}
				}
				break;
			case '\'':
				if (!isInQuotes && nextChar != '\'') {
					isInApos = (!isInApos);
					if (isStartedString) {
						isDone = true;
					} else {
						isStartedString = !isStartedString;
					}
				}
				break;
			
			default:
				break;
			}
			if (isInQuotes || isInApos) {
				attributeValue = attributeValue + c;
			} else {
				attributeName = attributeName + c;
			}
			if (isDone) {
				String[] attribute = new String[2];
				attribute[0] = attributeName;
				attribute[1] = attributeValue;
				attributes.add(attribute);
				attributeValue = "";
				attributeName = "";
				isDone = false;
			}
		}
		return attributes;
	}
	
}
