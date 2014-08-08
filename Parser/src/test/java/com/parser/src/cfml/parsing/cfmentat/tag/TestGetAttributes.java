package com.parser.src.cfml.parsing.cfmentat.tag;

import java.net.URL;
import java.util.Iterator;

import com.parser.main.cfmentat.tag.CFMLStartTag;

import junit.framework.TestCase;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.EndTagType;
import net.htmlparser.jericho.Source;

/**
 * @author denny
 */
public class TestGetAttributes extends TestCase {
	private static final String sourceUrlString = "/src/test/java/com/parser/data/tag/attribute/simpleTests.xml";
	private Source fSource;
	private CFMLStartTag fGenericStartTag;
	
	/**
	 * @throws java.lang.Exception
	 */
	public void setUp() throws Exception {
		fGenericStartTag = new CFMLStartTag("CFML generic tag", "<cf", ">", EndTagType.NORMAL, false, false, false);
		fSource = new Source(new URL(sourceUrlString));
	}
	
	/**
	 * Test method for {@link net.htmlparser.jericho.StartTagTypeCFML#getAttributes(java.lang.String)} .
	 */
	public void testGetAttributes() {
		Iterator<Element> tests = fSource.getFirstElement().getChildElements().iterator();
		String attributes, numattributes, testname;
		int i = 0;
		while (tests.hasNext()) {
			Element type = (Element) tests.next();
			attributes = type.getTextExtractor().toString();
			numattributes = type.getAttributeValue("numattributes");
			testname = type.getAttributeValue("name");
			assertEquals(testname + " (" + i + ") failed - getAttributes", Integer.parseInt(numattributes),
					fGenericStartTag.getAttributes(attributes).size());
			for (Object attr : fGenericStartTag.getAttributes(attributes)) {
				System.err.println(attr.toString());
			}
			System.out.println(type.getTextExtractor().toString());
			System.out.println(type.getAttributeValue("numattributes"));
			i++;
		}
	}
	
}
