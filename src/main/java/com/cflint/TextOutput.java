package com.cflint;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;

public class TextOutput {

	final static String newLine = System.getProperty("line.separator");
	public void output(final BugList bugList, final Writer sb) throws IOException {
		int counter = 0;
		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			sb.append("Issue");
			for (final BugInfo bugInfo : bugEntry.getValue()) {
				sb.append(newLine).append("Severity:").append(bugEntry.getValue().get(0).getSeverity());
				sb.append(newLine).append("Message code:").append(bugEntry.getValue().get(0).getMessageCode());
				counter++;
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
		sb.append(newLine).append("Total issues:" + counter);
		sb.flush();
		sb.close();
	}
}
