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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSONOutput {

	@SuppressWarnings("unchecked")
	public void output(final BugList bugList, final Writer writer) throws IOException {
		// final StringBuilder sb = new StringBuilder();
		
		JSONArray issues = new JSONArray();
		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			final Iterator<BugInfo> iterator = bugEntry.getValue().iterator();
			BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
			BugInfo prevbugInfo = null;

			while (bugInfo != null) {
				JSONObject issue = new JSONObject();
				issue.put("severity", notNull(bugEntry.getValue().get(0).getSeverity()));
				issue.put("id", bugEntry.getValue().get(0).getMessageCode());
				issue.put("message", bugEntry.getValue().get(0).getMessageCode());
				issue.put("category", "CFLINT");
				issue.put("abbrev", abbrev(bugEntry.getValue().get(0).getMessageCode()));
				final JSONArray locations = new JSONArray();
				issue.put("locations", locations);
				do {
					final JSONObject location = new JSONObject();
					location.put("file",notNull(bugInfo.getFilename()));
					location.put("fileName",filename(bugInfo.getFilename()));
					location.put("column",Integer.valueOf(bugInfo.getColumn()).toString());
					location.put("line",Integer.valueOf(bugInfo.getLine()).toString());
					location.put("message",notNull(bugInfo.getMessage()));
					location.put("variable",notNull(bugInfo.getVariable()));
					location.put("expression",notNull(bugInfo.getExpression()));
					locations.add(location);
					prevbugInfo = bugInfo;
					bugInfo = iterator.hasNext() ? iterator.next() : null;
				} while (isGrouped(prevbugInfo, bugInfo));
				issues.add(issue);
			}
		}
		issues.writeJSONString(writer);
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

	private CharSequence filename(final String filename) {
		if(filename == null){
			return "";
		}
		return filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'))+1);
	}

	private CharSequence abbrev(final String messageCode) {
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
