package com.parser.src.cfml.parsing.cfmentat.tag;

import java.util.List;

import com.parser.main.cfmentat.tag.CFMLStartTag;
import com.parser.main.cfmentat.tag.CFMLTags;
import com.parser.main.cfmentat.tag.StartTagTypeCfReturn;
import com.parser.main.cfmentat.tag.StartTagTypeCfSet;

import junit.framework.TestCase;
import net.htmlparser.jericho.EndTagType;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagType;

public class TestGetStartTagEnd extends TestCase {
	private StartTag parseAndGetFirstTag(String rawSource, StartTagType startTag) {
		// Create a source from the raw source
		Source source = new Source(rawSource);
		
		// Retrieve all the start tags
		List<StartTag> cftags = source.getAllStartTags(startTag);
		
		// Return the first tag found
		return cftags.get(0);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	public void setUp() throws Exception {
		CFMLTags.register();
	}
	
	public void testAllStartTags_length_randomWithString() {
		// Create and register the random, custom tag
		CFMLStartTag randomTag = new CFMLStartTag("test", "<cfrandom", ">", EndTagType.NORMAL, false);
		randomTag.register();
		
		String rawSource = "<cfrandom \"custom string\" />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, randomTag);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_returnWithNestedDoubleQuotes() {
		String rawSource = "<cfreturn \"one two \"\" three\" />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfReturn.getInstance());
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_returnWithNestedSingleQuotes() {
		String rawSource = "<cfreturn 'one two '' three' />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfReturn.getInstance());
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_returnWithoutAttribute() {
		String rawSource = "<cfreturn />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfReturn.getInstance());
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_returnWithVariable() {
		String rawSource = "<cfreturn blah />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfReturn.getInstance());
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_returnWithVariableAndStringConcatnation() {
		String rawSource = "<cfreturn blah & \"foo\" />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfReturn.getInstance());
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_returnWithVariableConcatnation() {
		String rawSource = "<cfreturn blah & foo />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfReturn.getInstance());
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_returnWithVariableInString() {
		String rawSource = "<cfreturn \"foo#bar#\" />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfReturn.getInstance());
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithAddedVariables() {
		String rawSource = "<cfset blah='#varName + foobar#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithArrayVariable() {
		String rawSource = "<cfset blah='#varName[120]#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithComment() {
		String rawSource = "<cfset <!--- fun='eeep' ---> blah='foo>'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithConcatenatedVariable() {
		String rawSource = "<cfset blah='#varName & foobar#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithDividedVariables() {
		String rawSource = "<cfset blah='#varName / foobar#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithDoubleQuotes() {
		String rawSource = "<cfset blah=\"foo\"/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithIntegerDivisionVariables() {
		String rawSource = "<cfset blah='#varName \\ foobar#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithModulusVariables() {
		String rawSource = "<cfset blah='#varName % foobar#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithMultipleAttributes() {
		String rawSource = "<cfset action=\"compileMapping\" type=\"web\" password=\"f00B4R\" virtual=\"/directory\" stoponerror=\"false\" />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithMultipleSets() {
		String rawSource = "<cfset blah='foo' />";
		String unmatched = "<cfset blah='wee' />";
		
		StartTag cftag = parseAndGetFirstTag(rawSource.concat(unmatched), StartTagTypeCfSet.INSTANCE);
		
		assertEquals((rawSource + unmatched).length() - unmatched.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithMultipliedVariables() {
		String rawSource = "<cfset blah='#varName * foobar#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithoutSelfClosing() {
		String rawSource = "<cfset blah='foo'>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithSelfClosing() {
		String rawSource = "<cfset blah='foo'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithSingleQuotes() {
		String rawSource = "<cfset blah='foo>'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithStructureReferencedVariable() {
		String rawSource = "<cfset blah='#varName['bar']#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithStructureVariable() {
		String rawSource = "<cfset blah='#varName.bar#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithSubtractedVariables() {
		String rawSource = "<cfset blah='#varName - foobar#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
	
	public void testAllStartTags_length_setWithVariable() {
		String rawSource = "<cfset blah='#varName#'/>";
		
		StartTag cftag = parseAndGetFirstTag(rawSource, StartTagTypeCfSet.INSTANCE);
		
		assertEquals(rawSource.length(), cftag.getEnd());
	}
}
