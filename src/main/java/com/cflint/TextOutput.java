package com.cflint;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;

public class TextOutput {

	public void output(final BugList bugList, final Writer sb) throws IOException {
		int counter = 0;
		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			sb.append("Issue");
			for (final BugInfo bugInfo : bugEntry.getValue()) {
				sb.append(System.getProperty("line.separator")).append("Severity:").append(bugEntry.getValue().get(0).getSeverity());
				sb.append(System.getProperty("line.separator")).append("Message code:").append(bugEntry.getValue().get(0).getMessageCode());
				counter++;
				sb.append(System.getProperty("line.separator")).append("\tFile:").append(bugInfo.getFilename());
				sb.append(System.getProperty("line.separator")).append("\tColumn:").append(Integer.valueOf(bugInfo.getColumn()).toString());
				sb.append(System.getProperty("line.separator")).append("\tLine:").append(Integer.valueOf(bugInfo.getLine()).toString());
				sb.append(System.getProperty("line.separator")).append("\t\tMessage:").append(bugInfo.getMessage());
				sb.append(System.getProperty("line.separator")).append("\t\tVariable:'").append(bugInfo.getVariable());
				sb.append("' in function: ").append(bugInfo.getFunction());
				sb.append(System.getProperty("line.separator")).append("\t\tExpression:").append(bugInfo.getExpression());
				sb.append(System.getProperty("line.separator"));
			}
		}
		sb.append(System.getProperty("line.separator")).append("Total issues:" + counter);
		sb.flush();
		sb.close();
	}
}
