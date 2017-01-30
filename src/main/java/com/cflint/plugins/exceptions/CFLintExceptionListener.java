package com.cflint.plugins.exceptions;

public interface CFLintExceptionListener {

    public void exceptionOccurred(Throwable exception, String messageCode, String filename, Integer line,
            Integer column, String functionName, String expression);

}
