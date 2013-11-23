package com.cfbugs;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

public class XMLOutput {

	public void output(final BugList bugList, final Writer writer) throws IOException {
		// final StringBuilder sb = new StringBuilder();
		writer.append("<issues>").append("\r\n");
		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			writer.append("<issue");
			writer.append(" severity=\"").append(bugEntry.getValue().get(0).getSeverity()).append("\"");
			writer.append(" id=\"").append(bugEntry.getValue().get(0).getMessageCode()).append("\"");
			writer.append(" message=\"").append(bugEntry.getValue().get(0).getMessageCode()).append("\"");
			writer.append(" category=\"CFBugs\"");
			writer.append(" abbrev=\"").append(abbrev(bugEntry.getValue().get(0).getMessageCode())).append("\"");
			writer.append(">");
			for (final BugInfo bugInfo : bugEntry.getValue()) {
				writer.append("<location");
				writer.append(" file=\"").append(bugInfo.getFilename()).append("\"");
				writer.append(" fileName=\"").append(filename(bugInfo.getFilename())).append("\"");
				writer.append(" column=\"").append(Integer.valueOf(bugInfo.getColumn()).toString()).append("\"");
				writer.append(" line=\"").append(Integer.valueOf(bugInfo.getLine()).toString()).append("\"");
				writer.append(" message=\"").append(bugInfo.getMessage()).append("\"");
				writer.append(" variable=\"").append(bugInfo.getVariable()).append("\"");
				//writer.append(" function=\"").append(bugInfo.getFunction()).append("\"");
				writer.append(">");
				writer.append("<Expression><![CDATA[").append(bugInfo.getExpression()).append("]]></Expression>");
				writer.append("</location>").append("\r\n");
			}
			writer.append("</issue>").append("\r\n");
		}
		writer.append("</issues>");
		writer.close();
	}

	public void outputFindBugs(final BugList bugList, final Writer writer) throws IOException, TransformerException {
		final StringWriter sw = new StringWriter();
		output(bugList, sw);
		// 1. Instantiate a TransformerFactory.
		final javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

		// 2. Use the TransformerFactory to process the stylesheet Source and
		// generate a Transformer.

		final InputStream is = getClass().getResourceAsStream("/findbugs/cfbugs-to-findbugs.xsl");
		System.out.println("-----------");
		System.out.println("----------- " + is.available());
		System.out.println("-----------");
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

}
