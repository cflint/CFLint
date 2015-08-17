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
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONOutput {

	JSONObject obj = new JSONObject();

	public JSONObject output(final BugList bugList, final Writer writer) throws IOException {
		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			final Iterator<BugInfo> iterator = bugEntry.getValue().iterator();
			BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
			BugInfo prevbugInfo = null;

			while (bugInfo != null) {
				obj.put("severity", bugEntry.getValue().get(0).getSeverity());
				obj.put("id", bugEntry.getValue().get(0).getMessageCode());
				obj.put("message", bugEntry.getValue().get(0).getMessageCode());
				//obj.put("category=CFLint\"");
				obj.put("abbrev", abbrev(bugEntry.getValue().get(0).getMessageCode()));
				
				do {
					// JSONObject arr = new JSONObject();
					// arr.put("file", bugInfo.getFilename());
					// arr.put("fileName", filename(bugInfo.getFilename()));
					// arr.put("column", Integer.valueOf(bugInfo.getColumn()).toString());
					// arr.put("line", Integer.valueOf(bugInfo.getLine()).toString());
					// arr.put("expression", bugInfo.getExpression() == null ? "" : bugInfo.getExpression());
					// obj.put("location", arr);
				 	prevbugInfo = bugInfo;
				 	bugInfo = iterator.hasNext() ? iterator.next() : null;
				} while (isGrouped(prevbugInfo, bugInfo));
			}
		}
		return obj;
	}

	private CharSequence filename(final String filename) {
		return filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')));
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

	List<String> CODE_GROUPBY_FUNCTION = Arrays.asList("PARSE_ERROR");

	private boolean isGrouped(final BugInfo prevbugInfo, final BugInfo bugInfo) {
		if (prevbugInfo == null || bugInfo == null) {
			return false;
		}
		// Different message types are not grouped
		// if (!safeEquals(prevbugInfo.getMessageCode(), bugInfo.getMessageCode())) {
		// 	return false;
		// }
		// // Different files are not grouped
		// if (!safeEquals(prevbugInfo.getFilename(), bugInfo.getFilename())) {
		// 	return false;
		// }
		// Some message codes (such as parse error are grouped at the function
		// level
		if (CODE_GROUPBY_FUNCTION.contains(bugInfo.getMessageCode())) {
			// if (safeEquals(prevbugInfo.getFunction(), bugInfo.getFunction())) {
			// 	return true;
			// }
		}
		return false;
	}

	// private boolean safeEquals(final String a, final String b) {
	// 	return a != null && b != null && a.equals(b);
	// }

	// public void outputFindBugs(final BugList bugList, final Writer writer) throws IOException, TransformerException {
	// 	final StringWriter sw = new StringWriter();
	// 	output(bugList, sw);
	// 	// 1. Instantiate a TransformerFactory.
	// 	final javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

	// 	// 2. Use the TransformerFactory to process the stylesheet Source and
	// 	// generate a Transformer.

	// 	final InputStream is = getClass().getResourceAsStream("/findbugs/cflint-to-findbugs.xsl");
	// 	final javax.xml.transform.Transformer transformer = tFactory
	// 			.newTransformer(new javax.xml.transform.stream.StreamSource(is));

	// 	// 3. Use the Transformer to transform an XML Source and send the
	// 	// output to a Result object.
	// 	transformer.transform(new StreamSource(new StringReader(sw.toString())),
	// 			new javax.xml.transform.stream.StreamResult(writer));
	// 	writer.close();
	// }

	// private CharSequence filename(final String filename) {
	// 	return filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')));
	// }

	// private CharSequence abbrev(final String messageCode) {
	// 	if (messageCode.length() <= 2) {
	// 		return messageCode;
	// 	}
	// 	final String[] ms = messageCode.split("_");
	// 	if (ms.length >= 2) {
	// 		return (ms[0].substring(0, 1) + ms[1].substring(0, 1)).toUpperCase();
	// 	} else {
	// 		return ms[0].substring(0, 2).toUpperCase();
	// 	}
	// }

	// String xmlEscapeText(final String t) {
	// 	if (t == null) {
	// 		return "";
	// 	}
	// 	final StringBuilder sb = new StringBuilder();
	// 	for (int i = 0; i < t.length(); i++) {
	// 		final char c = t.charAt(i);
	// 		switch (c) {
	// 		case '<':
	// 			sb.append("&lt;");
	// 			break;
	// 		case '>':
	// 			sb.append("&gt;");
	// 			break;
	// 		case '\"':
	// 			sb.append("&quot;");
	// 			break;
	// 		case '&':
	// 			sb.append("&amp;");
	// 			break;
	// 		case '\'':
	// 			sb.append("&apos;");
	// 			break;
	// 		default:
	// 			if (c > 0x7e) {
	// 				sb.append("&#" + ((int) c) + ";");
	// 			} else {
	// 				sb.append(c);
	// 			}
	// 		}
	// 	}
	// 	return sb.toString();
	// }
}
