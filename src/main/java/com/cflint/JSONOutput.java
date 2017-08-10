package com.cflint;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class JSONOutput {

    final List<String> CODE_GROUPBY_FUNCTION = Arrays.asList("PARSE_ERROR");
    boolean prettyPrint = true;

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(final boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public void output(final BugList bugList, final Writer writer, CFLintStats stats) throws IOException {
        final BugCounts counts = stats.getCounts();
        final JsonFactory jsonF = new JsonFactory();
        final JsonGenerator jg = jsonF.createGenerator(writer);

        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }

        // start global object
        jg.writeStartObject();

            // timestamp and version
            jg.writeStringField("version",Version.getVersion());
            jg.writeNumberField("timestamp",stats.getTimestamp());

            // start issues array
            jg.writeFieldName("issues");
            jg.writeStartArray();

            List<String> keys = new ArrayList<String>(bugList.getBugList().keySet());
            Collections.sort(keys);

            for (final String key : keys) {
                List<BugInfo> currentList = bugList.getBugList().get(key);
                final Iterator<BugInfo> iterator = currentList.iterator();
                BugInfo bugInfo = iterator.hasNext() ? iterator.next() : null;
                BugInfo prevbugInfo;

                while (bugInfo != null) {
                    final String severity = currentList.get(0).getSeverity();
                    final String code = currentList.get(0).getMessageCode();

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
                                jg.writeNumberField("column", bugInfo.getColumn());
                                jg.writeNumberField("line", bugInfo.getLine());
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

            // end issues array
            jg.writeEndArray();

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
                    for (final String severity : BugCounts.levels) {
                        if (counts.getSeverity(severity) > 0) {
                            jg.writeStartObject();
                            jg.writeStringField("severity", severity);
                            jg.writeNumberField("count", counts.getSeverity(severity));
                            jg.writeEndObject();
                        }
                    }
                // end countBySeverity array
                jg.writeEndArray();
            // end counts object
            jg.writeEndObject();

        // end global object
        jg.writeEndObject();

        jg.close();
        writer.close();
    }

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
