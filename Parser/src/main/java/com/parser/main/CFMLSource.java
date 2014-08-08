package com.parser.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.SourceFormatter;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagType;
import net.htmlparser.jericho.Tag;
import com.parser.main.cfmentat.tag.CFMLTags;
import com.parser.main.preferences.ParserPreferences;

public class CFMLSource {
	
	private Source fSource;
	
	public CFMLSource(String contents) {
		CFMLTags.register();
		fSource = new Source(contents);
		fSource.ignoreWhenParsing(fSource.getAllElements(CFMLTags.CFML_CONTENT));
	}
	
	public CFMLSource(String contents, ParserPreferences prefs) {
		CFMLTags.register(prefs);
		fSource = new Source(contents);
		fSource.ignoreWhenParsing(fSource.getAllElements(CFMLTags.CFML_CONTENT));
	}
	
	public CFMLSource(URL url) throws IOException {
		CFMLTags.register();
		fSource = new Source(url);
		fSource.ignoreWhenParsing(fSource.getAllElements(CFMLTags.CFML_CONTENT));
	}
	
	public CFMLSource(URL url, ParserPreferences prefs) throws IOException {
		CFMLTags.register(prefs);
		fSource = new Source(url);
		fSource.ignoreWhenParsing(fSource.getAllElements(CFMLTags.CFML_CONTENT));
	}
	
	public String getDebuggingInfo() {
		return fSource.getDebugInfo();
	}
	
	public List<Element> getAllElements(StartTagType startTagType) {
		return fSource.getAllElements(startTagType);
	}
	
	public List<Element> getChildElements() {
		return fSource.getChildElements();
	}
	
	public void ignoreWhenParsing(List allElements) {
		fSource.ignoreWhenParsing(allElements);
	}
	
	public String getCacheDebugInfo() {
		return fSource.getCacheDebugInfo();
	}
	
	public List<Element> getAllElements() {
		return fSource.getAllElements();
	}
	
	public SourceFormatter getSourceFormatter() {
		return fSource.getSourceFormatter();
	}
	
	public List<StartTag> getAllStartTags() {
		return fSource.getAllStartTags();
	}
	
	public OutputDocument getOutputDocument() {
		return new OutputDocument(fSource);
	}
	
	public int getRow(int begin) {
		return fSource.getRow(begin);
	}
	
	public List<StartTag> getAllCFMLTags() {
		// return fSource.getAllStartTags("cf");
		return getTagsByName("cf");
	}
	
	public ParserTag getTagAt(int i) {
		ParserTag parserTag = makeParserTag(fSource.getTagAt(i));
		return parserTag;
	}
	
	public List<StartTag> getTagsByName(String tagName) {
		List<StartTag> tags = new ArrayList<StartTag>();
		for (Iterator<?> i = getAllStartTags().iterator(); i.hasNext();) {
			StartTag tagStart = (StartTag) i.next();
			if (tagStart.getName().startsWith(tagName)) {
				tags.add(tagStart);
			}
		}
		return tags;
	}
	
	public ParserTag getEnclosingTag(int i) {
		Tag tag = fSource.getEnclosingTag(i);
		if (tag == null) {
			return null;
		}
		return makeParserTag(tag);
	}
	
	public ParserTag getNextTag(int i) {
		Tag tag = fSource.getNextTag(i);
		return makeParserTag(tag);
	}
	
	public ParserTag getPreviousTag(int i) {
		Tag encosingTag = fSource.getEnclosingTag(i);
		Tag parserTag = fSource.getPreviousTag(i);
		if (parserTag.getBegin() == encosingTag.getBegin())
			return makeParserTag(fSource.getPreviousTag(encosingTag.getBegin() - 1));
		return makeParserTag(fSource.getPreviousTag(i));
	}
	
	private ParserTag makeParserTag(net.htmlparser.jericho.Tag nextTag) {
		if (nextTag == null) {
			return null;
		}
		ParserTag newTag = new ParserTag(nextTag);
		return newTag;
	}
	
	private ParserTag makeParserTag(net.htmlparser.jericho.StartTag tag) {
		ParserTag newTag = new ParserTag(tag);
		return newTag;
		
	}
}
