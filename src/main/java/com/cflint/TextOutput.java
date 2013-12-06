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
			sb.append("\r\n severity:").append(bugEntry.getValue().get(0).getSeverity());
			sb.append("\r\n id:").append(bugEntry.getValue().get(0).getMessageCode());
			sb.append("\r\n message code:").append(bugEntry.getValue().get(0).getMessageCode());
			for (final BugInfo bugInfo : bugEntry.getValue()) {
				counter++;
				sb.append("\r\n\tfile:").append(bugInfo.getFilename());
				sb.append(Integer.valueOf(bugInfo.getColumn()).toString());
				sb.append(" [").append(Integer.valueOf(bugInfo.getLine()).toString()).append("]");
				sb.append("\r\n\t\tmessage:").append(bugInfo.getMessage());
				sb.append("\r\n\t\tvariable:'").append(bugInfo.getVariable());
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
