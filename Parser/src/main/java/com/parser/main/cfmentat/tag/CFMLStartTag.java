package com.parser.main.cfmentat.tag;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.EndTagType;
import net.htmlparser.jericho.ParseText;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagTypeGenericImplementation;

public class CFMLStartTag extends StartTagTypeGenericImplementation {
	public class ParseItemMatch {
		
	}
	
	public class AttributeItem {
		
		public AttributeItem(int linePos, int i, int inputPos, String attrName, String attrValue) {
			// TODO Auto-generated constructor stub
		}
		
	}
	
	static protected final String REG_ATTRIBUTES = "(?si)(\\w+)[\\s=]+(((\\x22|\\x27|#)((?!\\4).|\\4{2})*\\4))";
	private static CFMLStartTag INSTANCE = null;
	
	public CFMLStartTag(final String description, final String startDelimiter, final String closingDelimiter,
			final EndTagType correspondingEndTagType, final boolean isServerTag) {
		super(description, startDelimiter, closingDelimiter, correspondingEndTagType, isServerTag, false, false);
		INSTANCE = this;
	}
	
	public CFMLStartTag() {
		super("cf standard tag", "<cf", ">", EndTagType.NORMAL, false, true, false);
	}
	
	public CFMLStartTag(final String description, final String startDelimiter, final String closingDelimiter,
			final EndTagType correspondingEndTagType, final boolean isServerTag, final boolean hasAttributes,
			final boolean isNameAfterPrefixRequired) {
		super(description, startDelimiter, closingDelimiter, correspondingEndTagType, isServerTag, hasAttributes,
				isNameAfterPrefixRequired);
		INSTANCE = this;
	}
	
	public static CFMLStartTag getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CFMLStartTag();
		}
		return INSTANCE;
	}
	
	public boolean atEndOfAttributes(final Source source, final int pos, final boolean isClosingSlashIgnored) {
		final ParseText parseText = source.getParseText();
		return parseText.charAt(pos) == '>' || (parseText.containsAt("/>", pos));
	}
	
	protected int getEnd(final Source source, final int pos) {
		final ParseText text = source.getParseText();
		int endStartTagEnd = pos;
		boolean isInQuotes = false;
		boolean isInApos = false;
		for (int x = pos; x < text.length(); x++) {
			char c = text.charAt(x);
			switch (c) {
			case '>':
				if (!isInQuotes && !isInApos) {
					if (x > 2 && text.subSequence(x - 3, x).equals("---")) {
						// do nothing, this is a comment
					} else {
						return x + 1;
					}
				}
				break;
			case '"':
				if (!isInApos) {
					isInQuotes = (!isInQuotes);
				}
				break;
			case '\'':
				if (!isInQuotes) {
					isInApos = (!isInApos);
				}
				break;
			
			default:
				break;
			}
		}
		if (endStartTagEnd >= pos) {
			return endStartTagEnd + 1;
		}
		return endStartTagEnd;
	}
	
	public ArrayList getAttributes(String inData) {
		ArrayList attributes = new ArrayList();
		Matcher matcher;
		Pattern pattern;
		String attributeName, attributeValue;
		pattern = Pattern.compile(REG_ATTRIBUTES, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(inData);
		if (inData.trim().endsWith("&")) {
			userMessage(0, "stripAttributes", "Last attribute cannot be an ampersand", "ERR", null);
		}
		
		while (matcher.find()) {
			
			if (matcher.group(1) != null && matcher.group(2) != null) {
				String[] attribute = new String[2];
				
				attributeName = matcher.group(1).trim();
				attributeValue = matcher.group(2).trim();
				attributeValue = attributeValue.substring(1, attributeValue.length() - 1);
				attribute[0] = attributeName;
				attribute[1] = attributeValue;
				attributes.add(attribute);
				// System.out.println(attributeName + " = " +attributeValue);
			} else {
				System.out.println("CFParser::stripAttributes() - failed on |" + inData + "| with "
						+ matcher.groupCount() + " matches");
				// for (int i = 0; i<=matcher.groupCount(); i++) {
				// System.out.println("Match " + i + " : " + matcher.group(i));
				// }
			}
		}
		
		return attributes;
	}
	
	private void userMessage(int i, String string, String string2, String string3, Object object) {
		// TODO Auto-generated method stub
		System.out.println(string + string2);
	}
	
}