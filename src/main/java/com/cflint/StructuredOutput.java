package com.cflint;

import java.util.Arrays;
import java.util.List;

/**
 * Methods common to XML and JSON output.
 */
public class StructuredOutput {

    /**
     * Message codes grouped at the function level.
     */
    private static final List<String> CODE_GROUPBY_FUNCTION = Arrays.asList("PARSE_ERROR");

    /**
     * Returns true if previous bug and current bug are grouped.
     */
    public boolean isGrouped(final BugInfo prevbugInfo, final BugInfo bugInfo) {
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

        return CODE_GROUPBY_FUNCTION.contains(bugInfo.getMessageCode())
            && safeEquals(prevbugInfo.getFunction(), bugInfo.getFunction());
    }

    /**
     * Returns true if a equals b and both are not null.
     */
    public boolean safeEquals(final String a, final String b) {
        return a != null && b != null && a.equals(b);
    }

    /**
     * Filename from path.
     */
    public String filename(final String filename) {
        if (filename == null) {
            return "";
        }
        return filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')) + 1);
    }

    /**
     * Returns two letter message code abbreviation.
     */
    public String abbrev(final String messageCode) {
        if (messageCode == null) {
            return "";
        }
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

    /**
     * Return the value or an empty string if value is null.
     */
    public String notNull(final String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

}
