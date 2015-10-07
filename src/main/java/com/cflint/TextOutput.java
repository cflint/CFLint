package com.cflint;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;

public class TextOutput {

	final static String newLine = System.getProperty("line.separator");

	public enum Severity {
		FATAL, CRITICAL, ERROR, WARNING, CAUTION, INFO, COSMETIC 
	}

	public void output(final BugList bugList, final Writer sb) throws IOException {
		int counter = 0;
		Map<Severity, Integer> counts = new HashMap<Severity, Integer>();

		counts.put(Severity.FATAL, 0);
		counts.put(Severity.CRITICAL, 0);
		counts.put(Severity.ERROR, 0);
		counts.put(Severity.WARNING, 0);
		counts.put(Severity.CAUTION, 0);
		counts.put(Severity.INFO, 0);
		counts.put(Severity.COSMETIC, 0);

		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			sb.append("Issue");
			for (final BugInfo bugInfo : bugEntry.getValue()) {
				String severity = bugEntry.getValue().get(0).getSeverity();
				sb.append(newLine).append("Severity:").append(severity);
				sb.append(newLine).append("Message code:").append(bugEntry.getValue().get(0).getMessageCode());
				counter++;
				switch (severity) {
					case "FATAL":
						counts.put(Severity.FATAL, counts.get(Severity.FATAL) + 1);
						break;
					case "CRITICAL":
						counts.put(Severity.CRITICAL, counts.get(Severity.CRITICAL) + 1);
						break;
					case "ERROR":
						counts.put(Severity.ERROR, counts.get(Severity.ERROR) + 1);
						break;	
					case "WARNING":
						counts.put(Severity.WARNING, counts.get(Severity.WARNING) + 1);
						break;	
					case "CAUTION":
						counts.put(Severity.CAUTION, counts.get(Severity.CAUTION) + 1);
						break;
					case "INFO":
						counts.put(Severity.INFO, counts.get(Severity.INFO) + 1);
						break;
					case "COSMETIC":
						counts.put(Severity.COSMETIC, counts.get(Severity.COSMETIC) + 1);
						break;	
				}
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

		if (counts.get(Severity.FATAL) > 0) {
			sb.append(newLine).append("Total fatals:" + counts.get(Severity.FATAL) );
		}
		if (counts.get(Severity.CRITICAL) > 0) {
			sb.append(newLine).append("Total criticals:" + counts.get(Severity.CRITICAL) );
		}
		if (counts.get(Severity.ERROR) > 0) {
			sb.append(newLine).append("Total errors:" + counts.get(Severity.ERROR) );
		}
		if (counts.get(Severity.WARNING) > 0) {
			sb.append(newLine).append("Total warnings:" + counts.get(Severity.WARNING) );
		}
		if (counts.get(Severity.CAUTION) > 0) {
			sb.append(newLine).append("Total cautions:" + counts.get(Severity.CAUTION) );
		}
		if (counts.get(Severity.INFO) > 0) {
			sb.append(newLine).append("Total info:" + counts.get(Severity.INFO) );
		}
		if (counts.get(Severity.COSMETIC) > 0) {
			sb.append(newLine).append("Total cosmetic:" + counts.get(Severity.COSMETIC) );
		}
		sb.flush();
		sb.close();
	}
}
