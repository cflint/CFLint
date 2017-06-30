package com.cflint.xml.stax;

import java.io.File;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.cflint.*;
import com.cflint.xml.CFLintResultMarshaller;
import com.cflint.xml.MarshallerException;

import javanet.staxutils.IndentingXMLStreamWriter;

/**
 * Generates xml file with CFlint analyze results.
 */
public class DefaultCFlintResultMarshaller implements CFLintResultMarshaller {

    @Override
    public void output(BugList bugList, Writer writer, boolean showStats, CFLintStats stats) throws MarshallerException {

        try {
            final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw = new IndentingXMLStreamWriter(xmlOutputFactory.createXMLStreamWriter(writer));

            writeIssues(bugList, xtw, showStats, stats);

            xtw.flush();

        } catch (XMLStreamException e) {
            throw new MarshallerException(e);
        }
    }

    private void writeIssues(BugList bugList, XMLStreamWriter xtw, boolean showStats, CFLintStats stats) throws XMLStreamException {
        xtw.writeStartElement("issues");
        xtw.writeAttribute("version", Version.getVersion());

        BugCounts counts = stats.getCounts();

        for (BugInfo bug : bugList) {

            counts.add(bug.getMessageCode(), bug.getSeverity());

            writeIssue(xtw, bug);
        }

        if (showStats) {
            writeCounts(xtw, counts);
        }

        xtw.writeEndElement();
    }

    private void writeCounts(XMLStreamWriter xtw, BugCounts counts) throws XMLStreamException {
        xtw.writeStartElement("counts");

        for (String code : counts.bugTypes()) {
            xtw.writeStartElement("count");
            xtw.writeAttribute("code", valueOf(code));
            xtw.writeAttribute("count", valueOf(counts.getCode(code)));
            xtw.writeEndElement();
        }

        for (String severity : BugCounts.levels) {
            if (counts.getSeverity(severity) > 0) {
                xtw.writeStartElement("count");
                xtw.writeAttribute("severity", valueOf(severity));
                xtw.writeAttribute("count", valueOf(counts.getSeverity(severity)));
                xtw.writeEndElement();
            }
        }

        xtw.writeEndElement();
    }

    private void writeIssue(XMLStreamWriter xtw, BugInfo bug) throws XMLStreamException {

        xtw.writeStartElement("issue");
        xtw.writeAttribute("severity", valueOf(bug.getSeverity()));
        xtw.writeAttribute("id", valueOf(bug.getMessageCode()));
        xtw.writeAttribute("message", valueOf(bug.getMessageCode()));
        xtw.writeAttribute("category", valueOf("CFLint"));
        xtw.writeAttribute("abbrev", valueOf(abbrev(bug.getMessageCode())));

        writeLocation(xtw, bug);

        xtw.writeEndElement();
    }

    private void writeLocation(XMLStreamWriter xtw, BugInfo bug) throws XMLStreamException {
        xtw.writeStartElement("location");
        xtw.writeAttribute("file", valueOf(bug.getFilename()));
        xtw.writeAttribute("fileName", valueOf(new File(bug.getFilename()).getName()));
        xtw.writeAttribute("function", valueOf(bug.getFunction()));
        xtw.writeAttribute("column", valueOf(bug.getColumn()));
        xtw.writeAttribute("line", valueOf(bug.getLine()));
        xtw.writeAttribute("message", valueOf(bug.getMessage()));
        xtw.writeAttribute("variable", valueOf(bug.getVariable()));

        writeExpression(xtw, bug);

        xtw.writeEndElement();
    }

    private void writeExpression(XMLStreamWriter xtw, BugInfo bug) throws XMLStreamException {
        xtw.writeStartElement("Expression");

        escapeDeep(xtw, valueOf(bug.getExpression()));

        xtw.writeEndElement();
    }

    void escapeDeep(XMLStreamWriter xtw, String data) throws XMLStreamException {
        final String pattern = "]]>";

        final int offset = data.indexOf(pattern);

        if (offset != -1) {
            xtw.writeCData(escapeControlCharacters(data.substring(0, offset + 2)));
            escapeDeep(xtw, data.substring(offset + 2));
        } else {
            xtw.writeCData(escapeControlCharacters(data));
        }
    }

    private String escapeControlCharacters(String value) {
        StringBuilder sb = new StringBuilder(value);

        for (int i = 0; i < sb.length(); i++) {

            final int codePoint = sb.codePointAt(i);

            if (Character.isISOControl(codePoint)) {
                final String encode = UnicodeCharEncoder.encode(codePoint);
                sb.replace(i, i + 1, encode);
            }

        }

        return sb.toString();
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

    private String valueOf(int value) {
        return valueOf(String.valueOf(value));
    }

    private String valueOf(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    private static class UnicodeCharEncoder {
        private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F' };

        public static String encode(int c) {

            return "&#x" + toHex(((c >> 12) & 0xF)) + toHex(((c >> 8) & 0xF)) + toHex(((c >> 4) & 0xF)) + toHex(c & 0xF)
                    + ";";
        }

        public static char toHex(int val) {
            return DIGITS[val & 0xF];
        }

    }
}
