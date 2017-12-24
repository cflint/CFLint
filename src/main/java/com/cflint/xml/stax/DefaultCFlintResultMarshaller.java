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
    public void output(final BugList bugList, final Writer writer, final CFLintStats stats) throws MarshallerException {

        try {
            final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw = new IndentingXMLStreamWriter(xmlOutputFactory.createXMLStreamWriter(writer));

            writeIssues(bugList, xtw, stats);

            xtw.flush();

        } catch (XMLStreamException e) {
            throw new MarshallerException(e);
        }
    }

    private void writeIssues(final BugList bugList, final XMLStreamWriter xtw, final CFLintStats stats) throws XMLStreamException {
        xtw.writeStartElement("issues");
        xtw.writeAttribute("version", Version.getVersion());
        xtw.writeAttribute("timestamp", Long.toString(stats.getTimestamp()));

        BugCounts counts = stats.getCounts();

        for (BugInfo bug : bugList) {
            writeIssue(xtw, bug);
        }

        writeCounts(xtw, counts, stats);

        xtw.writeEndElement();
    }

    private void writeCounts(final XMLStreamWriter xtw, final BugCounts counts, final CFLintStats stats) throws XMLStreamException {
        xtw.writeStartElement("counts");
        xtw.writeAttribute("totalfiles", Long.toString(stats.getFileCount()));
        xtw.writeAttribute("totallines", stats.getTotalLines().toString());

        for (String code : counts.bugTypes()) {
            xtw.writeStartElement(Bugs.COUNT);
            xtw.writeAttribute(Bugs.CODE, valueOf(code));
            xtw.writeAttribute(Bugs.COUNT, valueOf(counts.getCode(code)));
            xtw.writeEndElement();
        }

        for (Levels severity : Levels.values()) {
            if (counts.getSeverity(severity) > 0) {
                xtw.writeStartElement(Bugs.COUNT);
                xtw.writeAttribute(Bugs.SEVERITY, valueOf(severity.toString()));
                xtw.writeAttribute(Bugs.COUNT, valueOf(counts.getSeverity(severity)));
                xtw.writeEndElement();
            }
        }

        xtw.writeEndElement();
    }

    private void writeIssue(final XMLStreamWriter xtw, final BugInfo bug) throws XMLStreamException {

        xtw.writeStartElement(Bugs.ISSUE);
        xtw.writeAttribute(Bugs.SEVERITY, valueOf(bug.getSeverity().toString()));
        xtw.writeAttribute(Bugs.ID, valueOf(bug.getMessageCode()));
        xtw.writeAttribute("message", valueOf(bug.getMessageCode()));
        xtw.writeAttribute("category", valueOf("CFLint"));
        xtw.writeAttribute("abbrev", valueOf(abbrev(bug.getMessageCode())));

        writeLocation(xtw, bug);

        xtw.writeEndElement();
    }

    private void writeLocation(final XMLStreamWriter xtw, final BugInfo bug) throws XMLStreamException {
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

    private void writeExpression(final XMLStreamWriter xtw, final BugInfo bug) throws XMLStreamException {
        xtw.writeStartElement("Expression");

        escapeDeep(xtw, valueOf(bug.getExpression()));

        xtw.writeEndElement();
    }

    private void escapeDeep(final XMLStreamWriter xtw, final String data) throws XMLStreamException {
        final String pattern = "]]>";

        final int offset = data.indexOf(pattern);

        if (offset != -1) {
            xtw.writeCData(escapeControlCharacters(data.substring(0, offset + 2)));
            escapeDeep(xtw, data.substring(offset + 2));
        } else {
            xtw.writeCData(escapeControlCharacters(data));
        }
    }

    private String escapeControlCharacters(final String value) {
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

    private String valueOf(final int value) {
        return valueOf(String.valueOf(value));
    }

    private String valueOf(final String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    private static class UnicodeCharEncoder {
        private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F' };

        public static String encode(final int c) {

            return "&#x" + toHex(((c >> 12) & 0xF)) + toHex(((c >> 8) & 0xF)) + toHex(((c >> 4) & 0xF)) + toHex(c & 0xF)
                    + ";";
        }

        public static char toHex(final int val) {
            return DIGITS[val & 0xF];
        }

    }
}
