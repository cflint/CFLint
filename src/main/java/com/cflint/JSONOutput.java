package com.cflint;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class JSONOutput {

	boolean prettyPrint = true;

	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	public void setPrettyPrint(final boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	public void output(final BugList bugList, final Writer writer, final boolean showStats) throws IOException {
		final BugCounts counts = new BugCounts();
		// final StringBuilder sb = new StringBuilder();
		final JsonFactory jsonF = new JsonFactory();
		final JsonGenerator jg = jsonF.createGenerator(writer);
		if (prettyPrint) {
			jg.useDefaultPrettyPrinter();
		}
		jg.writeStartArray();
		List<String> keys = new ArrayList<String>(bugList.getBugList().keySet());
		Collections.sort(keys);
		for (final String key: keys){
			List<BugInfo> currentList = bugList.getBugList().get(key);
			final Iterator<BugInfo> iterator = currentList.iterator();
			BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
			BugInfo prevbugInfo;

			while (bugInfo != null) {
				final String severity = currentList.get(0).getSeverity();
				final String code = currentList.get(0).getMessageCode();
				counts.add(code, severity);

				jg.writeStartObject();
				jg.writeStringField("severity", notNull(severity));
				jg.writeStringField("id", code);
				jg.writeStringField("message", code);
				jg.writeStringField("category", "CFLINT");
				jg.writeStringField("abbrev", abbrev(code));
				jg.writeFieldName("locations");
				jg.writeStartArray();
				do {
					jg.writeStartObject();
					jg.writeStringField("file", notNull(bugInfo.getFilename()));
					jg.writeStringField("fileName", filename(bugInfo.getFilename()));
					jg.writeStringField("function", filename(bugInfo.getFunction()));
					jg.writeStringField("column", Integer.valueOf(bugInfo.getColumn()).toString());
					jg.writeStringField("line", Integer.valueOf(bugInfo.getLine()).toString());
					jg.writeStringField("message", notNull(bugInfo.getMessage()));
					jg.writeStringField("variable", notNull(bugInfo.getVariable()));
					jg.writeStringField("expression", notNull(bugInfo.getExpression()));
					jg.writeEndObject();
					prevbugInfo = bugInfo;
					bugInfo = iterator.hasNext() ? iterator.next() : null;
				} while (isGrouped(prevbugInfo, bugInfo));
				jg.writeEndArray();
				jg.writeEndObject();
			}
		}

		if (showStats) {
			for (final String code : counts.bugTypes()) {
				jg.writeStartObject();
				jg.writeStringField("code", code);
				jg.writeStringField("count", Integer.toString(counts.getCode(code)));
				jg.writeEndObject();
			}
			for (final String severity : BugCounts.levels) {
				if (counts.getSeverity(severity) > 0) {
					jg.writeStartObject();
					jg.writeStringField("severity", severity);
					jg.writeStringField("count", Integer.toString(counts.getSeverity(severity)));
					jg.writeEndObject();
				}
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
		if (CODE_GROUPBY_FUNCTION.contains(bugInfo.getMessageCode())
				&& safeEquals(prevbugInfo.getFunction(), bugInfo.getFunction())) {
			return true;
		}
		return false;
	}

	private boolean safeEquals(final String a, final String b) {
		return a != null && b != null && a.equals(b);
	}

	public void outputFindBugs(final BugList bugList, final Writer writer, final boolean showStats)
			throws IOException, TransformerException {
		final StringWriter sw = new StringWriter();
		output(bugList, sw, showStats);
	}

	private String filename(final String filename) {
		if (filename == null) {
			return "";
		}
		return filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')) + 1);
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

	private String notNull(final String value) {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}
}
