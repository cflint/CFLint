package com.cflint;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Output bug list in XML format.
 */
public class XMLOutput extends StructuredOutput {

    /**
     * Line separator.
     */
    private static final String LINE_SEPARATOR = "line.separator";

    /**
     * Output bug list in XML format.
     */
    public void output(final BugList bugList, final Writer writer, final CFLintStats stats) throws IOException {
        final BugCounts counts = stats.getCounts();

        outputStartIssues(writer, stats);

        for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
            final Iterator<BugInfo> iterator = bugEntry.getValue().iterator();
            BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
            BugInfo prevbugInfo;

            while (bugInfo != null) {
                outputStartIssue(writer, bugEntry);
                do {
                    outputLocation(writer, bugInfo);
                    prevbugInfo = bugInfo;
                    bugInfo = iterator.hasNext() ? iterator.next() : null;
                } while (bugInfo != null && isGrouped(prevbugInfo, bugInfo));
                outputCloseIssue(writer);
            }
        }

        outputCounts(writer, stats, counts);
        outputCloseIssues(writer);
        writer.close();
    }

    /**
     * Output start of XML issues.
     */
    private void outputStartIssues(final Writer writer, final CFLintStats stats) throws IOException {
        writer.append("<issues version=\"" + Version.getVersion() + "\"")
                .append(" timestamp=\"" + Long.toString(stats.getTimestamp()) + "\">")
                .append(System.getProperty(LINE_SEPARATOR));
    }

    /**
     * Output close of XML issues.
     */
    private void outputCloseIssues(final Writer writer) throws IOException {
        writer.append("</issues>");
    }

    /**
     * Output start of XML issue.
     */
    private void outputStartIssue(final Writer writer, final Entry<String, List<BugInfo>> bugEntry) throws IOException {
        final String severity = bugEntry.getValue().get(0).getSeverity().toString();
        final String code = bugEntry.getValue().get(0).getMessageCode();
        writer.append("<issue");
        writer.append(" severity=\"").append(xmlEscapeText(severity)).append("\"");
        writer.append(" id=\"").append(xmlEscapeText(code)).append("\"");
        writer.append(" message=\"").append(xmlEscapeText(code)).append("\"");
        writer.append(" category=\"CFLint\"");
        writer.append(" abbrev=\"").append(abbrev(code)).append("\"");
        writer.append(">");
    }

    /**
     * Output start of XML issue location.
     */
    private void outputLocation(final Writer writer, final BugInfo bugInfo) throws IOException {
        writer.append("<location");
        writer.append(" file=\"").append(xmlEscapeText(bugInfo.getFilename())).append("\"");
        writer.append(" fileName=\"").append(xmlEscapeText(filename(bugInfo.getFilename()))).append("\"");
        writer.append(" function=\"").append(xmlEscapeText(filename(bugInfo.getFunction()))).append("\"");
        writer.append(" column=\"").append(Integer.toString(bugInfo.getColumn())).append("\"");
        writer.append(" line=\"").append(Integer.toString(bugInfo.getLine())).append("\"");
        writer.append(" message=\"").append(xmlEscapeText(bugInfo.getMessage())).append("\"");
        writer.append(" variable=\"").append(xmlEscapeText(bugInfo.getVariable())).append("\"");
        writer.append(">");
        writer.append("<Expression><![CDATA[")
                .append(bugInfo.getExpression() == null ? ""
                        : bugInfo.getExpression().replace("<![CDATA[", "").replace("]]>", ""))
                .append("]]></Expression>");
        writer.append("</location>").append(System.getProperty(LINE_SEPARATOR));
    }

    /**
     * Output end of XML issue.
     */
    private void outputCloseIssue(Writer writer) throws IOException {
        writer.append("</issue>").append(System.getProperty(LINE_SEPARATOR));
    }

    /**
     * Output XML count statistics.
     */
    private void outputCounts(final Writer writer, final CFLintStats stats, final BugCounts counts) throws IOException {
        writer.append("<counts");
        writer.append(" totalfiles=\"").append(Long.toString(stats.getFileCount())).append("\"");
        writer.append(" totallines=\"").append(stats.getTotalLines().toString()).append("\">").append(System.getProperty(LINE_SEPARATOR));

        for (final String code : counts.bugTypes()) {
            writer.append("<count");
            writer.append(" code=\"").append(code).append("\"");
            writer.append(" count=\"").append(Integer.toString(counts.getCode(code))).append("\" />");
            writer.append(System.getProperty(LINE_SEPARATOR));
        }

        for (final Levels severity : Levels.values()) {
            if (counts.getSeverity(severity) > 0) {
                writer.append("<count");
                writer.append(" severity=\"").append(severity.toString()).append("\"");
                writer.append(" count=\"").append(Integer.toString(counts.getSeverity(severity))).append("\" />");
                writer.append(System.getProperty(LINE_SEPARATOR));
            }
        }

        writer.append("</counts>").append(System.getProperty(LINE_SEPARATOR));
    }

    /**
     * Output findbugs XML format.
     */
    public void outputFindBugs(final BugList bugList, final Writer writer, final CFLintStats stats)
            throws IOException, TransformerException {

        final StringWriter sw = new StringWriter();
        output(bugList, sw, stats);

        // 1. Instantiate a TransformerFactory.
        final javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

        // 2. Use the TransformerFactory to process the stylesheet Source and generate a Transformer.
        final InputStream is = getClass().getResourceAsStream("/findbugs/cflint-to-findbugs.xsl");
        final javax.xml.transform.Transformer transformer = tFactory.newTransformer(new StreamSource(is));

        // 3. Use the Transformer to transform an XML Source and send the output to a Result object.
        transformer.transform(new StreamSource(new StringReader(sw.toString())), new StreamResult(writer));

        writer.close();
    }

    /**
     * XML escape a character sequence.
     */
    private String xmlEscapeText(final CharSequence t) {
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
                break;
            }
        }
        return sb.toString();
    }
}
