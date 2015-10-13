package com.cflint;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class JSONOutput {

	@SuppressWarnings("unchecked")
	public void output(final BugList bugList, final Writer writer) throws IOException {
		// final StringBuilder sb = new StringBuilder();
		JsonFactory jsonF = new JsonFactory();
		JsonGenerator jg = jsonF.createGenerator(writer);
		jg.writeStartArray();
		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			final Iterator<BugInfo> iterator = bugEntry.getValue().iterator();
			BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
			BugInfo prevbugInfo = null;

			while (bugInfo != null) {
				jg.writeStartObject();
				jg.writeStringField("severity", notNull(bugEntry.getValue().get(0).getSeverity()));
				jg.writeStringField("id", bugEntry.getValue().get(0).getMessageCode());
				jg.writeStringField("message", bugEntry.getValue().get(0).getMessageCode());
				jg.writeStringField("category", "CFLINT");
				jg.writeStringField("abbrev", abbrev(bugEntry.getValue().get(0).getMessageCode()));
				do {
					jg.writeObjectFieldStart("location");
					jg.writeStringField("file",notNull(bugInfo.getFilename()));
					jg.writeStringField("fileName",filename(bugInfo.getFilename()));
					jg.writeStringField("column",Integer.valueOf(bugInfo.getColumn()).toString());
					jg.writeStringField("line",Integer.valueOf(bugInfo.getLine()).toString());
					jg.writeStringField("message",notNull(bugInfo.getMessage()));
					jg.writeStringField("variable",notNull(bugInfo.getVariable()));
					jg.writeStringField("expression",notNull(bugInfo.getExpression()));
					jg.writeEndObject();
					prevbugInfo = bugInfo;
					bugInfo = iterator.hasNext() ? iterator.next() : null;
				} while (isGrouped(prevbugInfo, bugInfo));
				jg.writeEndObject();
			}
		}
		jg.writeEndArray();
		jg.close();
		writer.close();
	}

	List<String> CODE_GROUPBY_FUNCTION = Arrays.asList("PARSE_ERROR");

	private boolean isGrouped(final BugInfo prevbugInfo, final BugInfo bugInfo) {
		if (prevbugInfo == null || bugInfo == null) {
			return false;
		}
		// Different message types are not grouped
		if (!safeEquals(prevbugInfo.getMessageCode(), bugInfo.getMessageCode())) {
			return false;
		}
		// Different files are not grouped
		if (!safeEquals(prevbugInfo.getFilename(), bugInfo.getFilename())) {
			return false;
		}
		// Some message codes (such as parse error are grouped at the function
		// level
		if (CODE_GROUPBY_FUNCTION.contains(bugInfo.getMessageCode())) {
			if (safeEquals(prevbugInfo.getFunction(), bugInfo.getFunction())) {
				return true;
			}
		}
		return false;
	}

	private boolean safeEquals(final String a, final String b) {
		return a != null && b != null && a.equals(b);
	}

	public void outputFindBugs(final BugList bugList, final Writer writer) throws IOException, TransformerException {
		final StringWriter sw = new StringWriter();
		output(bugList, sw);
	}

	private String filename(final String filename) {
		if(filename == null){
			return "";
		}
		return filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'))+1);
	}

	private String abbrev(final String messageCode) {
		if (messageCode.length() <= 2) {
			return messageCode;
		}
		final String[] ms = messageCode.split("_");
		if (ms.length >= 2) {
			return (ms[0].substring(0, 1) + ms[1].substring(0, 1)).toUpperCase();
		} else {
			return ms[0].substring(0, 2).toUpperCase();
		}
	}

	private String notNull(String value){
		if (value ==  null){
			return "";
		}else{
			return value;
		}
	}
}
