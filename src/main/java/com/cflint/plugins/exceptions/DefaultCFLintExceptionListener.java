package com.cflint.plugins.exceptions;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.BugInfo.BugInfoBuilder;

/**
 * Default listener which treats Linter errors (file reading, and parsing) as bugs.
 * @author ryaneberly
 *
 */
public class DefaultCFLintExceptionListener implements CFLintExceptionListener {

	BugList bugs;

	public DefaultCFLintExceptionListener(BugList bugs) {
		super();
		this.bugs = bugs;
	}

	public void exceptionOccurred(final Exception exception, final String messageCode, final String filename,
			final Integer line, final Integer column, final String functionName, final String expression) {
		final BugInfoBuilder bugInfoBuilder = new BugInfo.BugInfoBuilder();
		bugInfoBuilder.setMessageCode(messageCode).setFilename(filename)
				.setSeverity("ERROR");
		if("PARSE_ERROR".equals(messageCode)){
			bugInfoBuilder.setMessage("Unable to parse");
		}else{
			bugInfoBuilder.setMessage(exception.getMessage());
		}
		bugInfoBuilder.setExpression(expression);
		bugInfoBuilder.setFunction(functionName);
		bugs.add(bugInfoBuilder.build());
	}

}
