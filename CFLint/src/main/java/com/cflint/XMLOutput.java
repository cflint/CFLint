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

public class XMLOutput {

	public void output(final BugList bugList, final Writer writer) throws IOException {
		// final StringBuilder sb = new StringBuilder();
		writer.append("<issues>").append("\r\n");
		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			final Iterator<BugInfo> iterator = bugEntry.getValue().iterator();
			BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
			BugInfo prevbugInfo = null;

			while (bugInfo != null) {
				writer.append("<issue");
				writer.append(" severity=\"").append(bugEntry.getValue().get(0).getSeverity()).append("\"");
				writer.append(" id=\"").append(bugEntry.getValue().get(0).getMessageCode()).append("\"");
				writer.append(" message=\"").append(bugEntry.getValue().get(0).getMessageCode()).append("\"");
				writer.append(" category=\"CFLint\"");
				writer.append(" abbrev=\"").append(abbrev(bugEntry.getValue().get(0).getMessageCode())).append("\"");
				writer.append(">");
				do {
					writer.append("<location");
					writer.append(" file=\"").append(bugInfo.getFilename()).append("\"");
					writer.append(" fileName=\"").append(filename(bugInfo.getFilename())).append("\"");
					writer.append(" column=\"").append(Integer.valueOf(bugInfo.getColumn()).toString()).append("\"");
					writer.append(" line=\"").append(Integer.valueOf(bugInfo.getLine()).toString()).append("\"");
					writer.append(" message=\"").append(xmlEscapeText(bugInfo.getMessage())).append("\"");
					writer.append(" variable=\"").append(xmlEscapeText(bugInfo.getVariable())).append("\"");
					writer.append(">");
					writer.append("<Expression><![CDATA[")
							.append(bugInfo.getExpression() == null ? "" : bugInfo.getExpression()
									.replace("<![CDATA[", "").replace("]]>", "")).append("]]></Expression>");
					writer.append("</location>").append("\r\n");
					prevbugInfo = bugInfo;
					bugInfo = iterator.hasNext() ? iterator.next() : null;
				} while (isGrouped(prevbugInfo, bugInfo));
				// writer.append(" function=\"").append(bugInfo.getFunction()).append("\"");
				writer.append("</issue>").append("\r\n");
			}
		}
		writer.append("</issues>");
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
		// 1. Instantiate a TransformerFactory.
		final javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

		// 2. Use the TransformerFactory to process the stylesheet Source and
		// generate a Transformer.

		final InputStream is = getClass().getResourceAsStream("/findbugs/cflint-to-findbugs.xsl");
		final javax.xml.transform.Transformer transformer = tFactory
				.newTransformer(new javax.xml.transform.stream.StreamSource(is));

		// 3. Use the Transformer to transform an XML Source and send the
		// output to a Result object.
		transformer.transform(new StreamSource(new StringReader(sw.toString())),
				new javax.xml.transform.stream.StreamResult(writer));
		writer.close();
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

	String xmlEscapeText(final String t) {
		if (t == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < t.length(); i++) {
			final char c = t.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				if (c > 0x7e) {
					sb.append("&#" + ((int) c) + ";");
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
}
