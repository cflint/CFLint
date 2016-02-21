
package com.cflint;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;

public class TextOutput {

	final static String newLine = System.getProperty("line.separator");

	public void output(final BugList bugList, final Writer sb, final boolean showStats) throws IOException {
		BugCounts counts = new BugCounts();

		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			sb.append("Issue");
			for (final BugInfo bugInfo : bugEntry.getValue()) {
				String severity = bugEntry.getValue().get(0).getSeverity();
				String code = bugEntry.getValue().get(0).getMessageCode();
				counts.add(code, severity);
				sb.append(newLine).append("Severity:").append(severity);
				sb.append(newLine).append("Message code:").append(code);
				sb.append(newLine).append("\tFile:").append(bugInfo.getFilename());
				sb.append(newLine).append("\tColumn:").append(Integer.valueOf(bugInfo.getColumn()).toString());
				sb.append(newLine).append("\tLine:").append(Integer.valueOf(bugInfo.getLine()).toString());
				sb.append(newLine).append("\t\tMessage:").append(bugInfo.getMessage());
				sb.append(newLine).append("\t\tVariable:'").append(bugInfo.getVariable());
				sb.append("' in function: ").append(bugInfo.getFunction());
				sb.append(newLine).append("\t\tExpression:").append(bugInfo.getExpression());
				sb.append(newLine);
			}
		}

		if (showStats) {
			sb.append(newLine).append(newLine).append("Issue counts:" + counts.noBugTypes());

			for (String code : counts.bugTypes()) {
				sb.append(newLine).append(code + ":" + counts.getCode(code));
			}

			sb.append(newLine).append(newLine).append("Total issues:" + counts.noBugs());

			for (String severity:BugCounts.levels)
			{
				if (counts.getSeverity(severity) > 0) {
					sb.append(newLine).append("Total " + severity.toLowerCase()+ "s:" + counts.getSeverity(severity));
				}
			}
		}

		sb.append(newLine);
		sb.flush();
		sb.close();
	}
}
