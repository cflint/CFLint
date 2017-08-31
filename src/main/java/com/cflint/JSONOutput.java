package com.cflint;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Output bug list in JSON format.
 */
public class JSONOutput extends StructuredOutput {

    /**
     * Do we want pretty output?
     */
    private boolean prettyPrint = true;

    /**
     * Return true if pretty output.
     */
    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    /**
     * Turn on / off pretty print.
     */
    public void setPrettyPrint(final boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    /**
     * Output bug list in JSON format.
     */
    public void output(final BugList bugList, final Writer writer, final CFLintStats stats) throws IOException {
        final BugCounts counts = stats.getCounts();
        final JsonFactory jsonF = new JsonFactory();
        final JsonGenerator jg = jsonF.createGenerator(writer);

        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }

        outputStart(stats, jg);
        outputStartIssues(jg);

        List<String> keys = new ArrayList<>(bugList.getBugList().keySet());
        Collections.sort(keys);

        for (final String key : keys) {
            List<BugInfo> currentList = bugList.getBugList().get(key);
            final Iterator<BugInfo> iterator = currentList.iterator();
            BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
            BugInfo prevbugInfo;

            while (bugInfo != null) {
                final Levels severity = currentList.get(0).getSeverity();
                final String code = currentList.get(0).getMessageCode();

                outputStartIssue(jg, severity, code);
                do {
                    outputLocation(jg, bugInfo);
                    prevbugInfo = bugInfo;
                    bugInfo = iterator.hasNext() ? iterator.next() : null;
                } while (bugInfo != null && isGrouped(prevbugInfo, bugInfo));
                outputCloseIssue(jg);
            }
        }
        
        outputCloseIssues(jg);
        outputCounts(stats, counts, jg);
        outputEnd(jg);
        writer.close();
    }


    /**
     * Output JSON start of global object and version info.
     */
    private void outputStart(CFLintStats stats, JsonGenerator jg) throws IOException {
        jg.writeStartObject();

        // timestamp and version
        jg.writeStringField("version", Version.getVersion());
        jg.writeNumberField("timestamp", stats.getTimestamp());
    }

    /**
     * Output JSON end of global object.
     */
    private void outputEnd(JsonGenerator jg) throws IOException {
        // end global object
        jg.writeEndObject();

        jg.close();
    }

    /**
     * Output JSON start of issues.
     */
    private void outputStartIssues(JsonGenerator jg) throws IOException {
        // start issues array
        jg.writeFieldName("issues");
        jg.writeStartArray();
    }

    /**
     * Output JSON end of issues.
     */
    private void outputCloseIssues(JsonGenerator jg) throws IOException {
        // end issues array
        jg.writeEndArray();
    }

    /**
     * Output JSON start of an issue.
     */
    private void outputStartIssue(JsonGenerator jg, Levels severity, String code) throws IOException {
        jg.writeStartObject();
        jg.writeStringField("severity", severity.toString());
        jg.writeStringField("id", code);
        jg.writeStringField("message", code);
        jg.writeStringField("category", "CFLINT");
        jg.writeStringField("abbrev", abbrev(code));
        jg.writeFieldName("locations");
        jg.writeStartArray();
    }

    /**
     * Output JSON issue location.
     */
    private void outputLocation(JsonGenerator jg, BugInfo bugInfo) throws IOException {
        jg.writeStartObject();
        jg.writeStringField("file", notNull(bugInfo.getFilename()));
        jg.writeStringField("fileName", filename(bugInfo.getFilename()));
        jg.writeStringField("function", filename(bugInfo.getFunction()));
        jg.writeNumberField("column", bugInfo.getColumn());
        jg.writeNumberField("line", bugInfo.getLine());
        jg.writeStringField("message", notNull(bugInfo.getMessage()));
        jg.writeStringField("variable", notNull(bugInfo.getVariable()));
        jg.writeStringField("expression", notNull(bugInfo.getExpression()));
        jg.writeEndObject();
    }

    /**
     * Output JSON end of issue.
     */
    private void outputCloseIssue(JsonGenerator jg) throws IOException {
        jg.writeEndArray();
        jg.writeEndObject();
    }

    /**
     * Output JSON count statistics.
     */
    private void outputCounts(CFLintStats stats, BugCounts counts, JsonGenerator jg) throws IOException {
        // start counts object
        jg.writeFieldName("counts");
        jg.writeStartObject();
        jg.writeNumberField("totalFiles", stats.getFileCount());
        // totalLines has to be separated into writing the field name and the number - .writeNumberField() can't deal with BigInt
        jg.writeFieldName("totalLines");
        jg.writeNumber(stats.getTotalLines());
        // start countByCode array
        jg.writeFieldName("countByCode");
        jg.writeStartArray();
        for (final String code : counts.bugTypes()) {
            jg.writeStartObject();
            jg.writeStringField("code", code);
            jg.writeNumberField("count", counts.getCode(code));
            jg.writeEndObject();
        }
        // end countByCode array

        jg.writeEndArray();
        // start countBySeverity array
        jg.writeFieldName("countBySeverity");
        jg.writeStartArray();
        for (final Levels severity : Levels.values()) {
            if (counts.getSeverity(severity) > 0) {
                jg.writeStartObject();
                jg.writeStringField("severity", severity.toString());
                jg.writeNumberField("count", counts.getSeverity(severity));
                jg.writeEndObject();
            }
        }
        // end countBySeverity array
        jg.writeEndArray();
        // end counts object
        jg.writeEndObject();
    }
}
