package com.cflint.plugins.exceptions;

public interface CFLintExceptionListener {

	public void exceptionOccurred(Exception exception, String messageCode,
			String filename, Integer line, Integer column, String functionName,
			String expression);

}
