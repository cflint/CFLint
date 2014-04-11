package com.cflint;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

public class StdoutOutput {

	public void output(final BugList bugList) throws IOException {
		int counter = 0;
		for (final Entry<String, List<BugInfo>> bugEntry : bugList.getBugList().entrySet()) {
			System.out.println("Issue");
			System.out.println("severity:" + bugEntry.getValue().get(0).getSeverity());
			System.out.println("id:" + bugEntry.getValue().get(0).getMessageCode());
			System.out.println("message code:" + bugEntry.getValue().get(0).getMessageCode());
			for (final BugInfo bugInfo : bugEntry.getValue()) {
				counter++;
				System.out.println("\tfile:" + bugInfo.getFilename());
				System.out.println("\t\tColumn: " + Integer.valueOf(bugInfo.getColumn()).toString());
				System.out.println("\t\tLine: " + Integer.valueOf(bugInfo.getLine()).toString());
				System.out.println("\t\tmessage:" + bugInfo.getMessage());
				System.out.println("\t\tvariable:'" + bugInfo.getVariable() + "' in function: " + bugInfo.getFunction());
				System.out.println("\t\tExpression:" + bugInfo.getExpression());
			}
		}
	}
}
