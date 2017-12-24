
package com.cflint;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;

public class TextOutput {

    private static final String NEW_LINE = System.getProperty("line.separator");

    public void output(final BugList bugList, final Writer sb, CFLintStats stats) throws IOException {
        final BugCounts counts = stats.getCounts();

        for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
            sb.append("Issue");
            for (final BugInfo bugInfo : bugEntry.getValue()) {
                final String severity = bugEntry.getValue().get(0).getSeverity().toString();
                final String code = bugEntry.getValue().get(0).getMessageCode();
                sb.append(NEW_LINE).append("Severity:").append(severity);
                sb.append(NEW_LINE).append("Message code:").append(code);
                sb.append(NEW_LINE).append("\tFile:").append(bugInfo.getFilename());
                sb.append(NEW_LINE).append("\tColumn:").append(Integer.toString(bugInfo.getColumn()));
                sb.append(NEW_LINE).append("\tLine:").append(Integer.toString(bugInfo.getLine()));
                sb.append(NEW_LINE).append("\t\tMessage:").append(bugInfo.getMessage());
                sb.append(NEW_LINE).append("\t\tVariable:'").append(bugInfo.getVariable());
                sb.append("' in function: ").append(bugInfo.getFunction());
                sb.append(NEW_LINE).append("\t\tExpression:").append(bugInfo.getExpression());
                sb.append(NEW_LINE);
            }
        }

        sb.append(NEW_LINE).append(NEW_LINE).append("Total files:" + stats.getFileCount());
        sb.append(NEW_LINE).append("Total lines:" + stats.getTotalLines());

        sb.append(NEW_LINE).append(NEW_LINE).append("Issue counts:" + counts.noBugTypes());

        for (final String code : counts.bugTypes()) {
            sb.append(NEW_LINE).append(code + ":" + counts.getCode(code));
        }

        sb.append(NEW_LINE).append(NEW_LINE).append("Total issues:" + counts.noBugs());

        for (final Levels severity : Levels.values()) {
            if (counts.getSeverity(severity) > 0) {
                sb.append(NEW_LINE).append("Total " + severity.toString().toLowerCase() + "s:" + counts.getSeverity(severity));
            }
        }

        sb.append(NEW_LINE);
        sb.flush();
        sb.close();
    }
}
