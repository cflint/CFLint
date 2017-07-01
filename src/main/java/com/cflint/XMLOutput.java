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
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLOutput {

    public static final String LINE_SEPARATOR = "line.separator";

    public void output(final BugList bugList, final Writer writer, final boolean showStats, final CFLintStats stats) throws IOException {
        final BugCounts counts = new BugCounts();
        writer.append("<issues version=\"" + Version.getVersion() + "\"")
                .append(" timestamp=\"" + Long.toString(stats.getTimestamp()) + "\">")
                .append(System.getProperty(LINE_SEPARATOR));
        for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
            final Iterator<BugInfo> iterator = bugEntry.getValue().iterator();
            BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
            BugInfo prevbugInfo;

            while (bugInfo != null) {
                final String severity = bugEntry.getValue().get(0).getSeverity();
                final String code = bugEntry.getValue().get(0).getMessageCode();
                counts.add(code, severity);
                writer.append("<issue");
                writer.append(" severity=\"").append(xmlEscapeText(severity)).append("\"");
                writer.append(" id=\"").append(xmlEscapeText(code)).append("\"");
                writer.append(" message=\"").append(xmlEscapeText(code)).append("\"");
                writer.append(" category=\"CFLint\"");
                writer.append(" abbrev=\"").append(abbrev(code)).append("\"");
                writer.append(">");
                do {
                    writer.append("<location");
                    writer.append(" file=\"").append(xmlEscapeText(bugInfo.getFilename())).append("\"");
                    writer.append(" fileName=\"").append(xmlEscapeText(filename(bugInfo.getFilename()))).append("\"");
                    writer.append(" function=\"").append(xmlEscapeText(filename(bugInfo.getFunction()))).append("\"");
                    writer.append(" column=\"").append(Integer.valueOf(bugInfo.getColumn()).toString()).append("\"");
                    writer.append(" line=\"").append(Integer.valueOf(bugInfo.getLine()).toString()).append("\"");
                    writer.append(" message=\"").append(xmlEscapeText(bugInfo.getMessage())).append("\"");
                    writer.append(" variable=\"").append(xmlEscapeText(bugInfo.getVariable())).append("\"");
                    writer.append(">");
                    writer.append("<Expression><![CDATA[")
                            .append(bugInfo.getExpression() == null ? ""
                                    : bugInfo.getExpression().replace("<![CDATA[", "").replace("]]>", ""))
                            .append("]]></Expression>");
                    writer.append("</location>").append(System.getProperty(LINE_SEPARATOR));
                    prevbugInfo = bugInfo;
                    bugInfo = iterator.hasNext() ? iterator.next() : null;
                } while (isGrouped(prevbugInfo, bugInfo));
                // writer.append("
                // function=\"").append(bugInfo.getFunction()).append("\"");
                writer.append("</issue>").append(System.getProperty(LINE_SEPARATOR));
            }
        }

        if (showStats) {
            writer.append("<counts");
            writer.append(" totalfiles=\"").append(Long.toString(stats.getFileCount())).append("\"");
            writer.append(" totalsize=\"").append(stats.getTotalSize().toString()).append("\">").append(System.getProperty(LINE_SEPARATOR));

            for (final String code : counts.bugTypes()) {
                writer.append("<count");
                writer.append(" code=\"").append(code).append("\"");
                writer.append(" count=\"").append(Integer.toString(counts.getCode(code))).append("\" />");
                writer.append(System.getProperty(LINE_SEPARATOR));
            }

            for (final String severity : BugCounts.levels) {
                if (counts.getSeverity(severity) > 0) {
                    writer.append("<count");
                    writer.append(" severity=\"").append(severity).append("\"");
                    writer.append(" count=\"").append(Integer.toString(counts.getSeverity(severity))).append("\" />");
                    writer.append(System.getProperty(LINE_SEPARATOR));
                }
            }

            writer.append("</counts>").append(System.getProperty(LINE_SEPARATOR));
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
        return (CODE_GROUPBY_FUNCTION.contains(bugInfo.getMessageCode())
                && safeEquals(prevbugInfo.getFunction(), bugInfo.getFunction()));
    }

    private boolean safeEquals(final String a, final String b) {
        return a != null && b != null && a.equals(b);
    }

    public void outputFindBugs(final BugList bugList, final Writer writer, final boolean showStats, final CFLintStats stats)
            throws IOException, TransformerException {

        final StringWriter sw = new StringWriter();
        output(bugList, sw, showStats, stats);

        // 1. Instantiate a TransformerFactory.
        final javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

        // 2. Use the TransformerFactory to process the stylesheet Source and generate a Transformer.
        final InputStream is = getClass().getResourceAsStream("/findbugs/cflint-to-findbugs.xsl");
        final javax.xml.transform.Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(is));

        // 3. Use the Transformer to transform an XML Source and send the output to a Result object.
        transformer.transform(new StreamSource(new StringReader(sw.toString())), new StreamResult(writer));

        writer.close();
    }

    private CharSequence filename(final String filename) {
        if (filename == null) {
            return "";
        }
        return filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')) + 1);
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

    String xmlEscapeText(final CharSequence t) {
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
