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
				sb.append("\r\nSeverity:").append(bugEntry.getValue().get(0).getSeverity());
				sb.append("\r\nMessage code:").append(bugEntry.getValue().get(0).getMessageCode());
				counter++;
				sb.append("\r\n\tFile:").append(bugInfo.getFilename());
				sb.append("\r\n\tColumn:").append(Integer.valueOf(bugInfo.getColumn()).toString());
				sb.append("\r\n\tLine:").append(Integer.valueOf(bugInfo.getLine()).toString());
				sb.append("\r\n\t\tMessage:").append(bugInfo.getMessage());
				sb.append("\r\n\t\tVariable:'").append(bugInfo.getVariable());
				sb.append("' in function: ").append(bugInfo.getFunction());
				sb.append("\r\n\t\tExpression:").append(bugInfo.getExpression());
				sb.append("\r\n");
			}
		}
		sb.append("\r\nTotal issues:" + counter);
		sb.flush();
		sb.close();
	}
}
