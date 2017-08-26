package com.cflint.exception;

/**
 * Exception class to report errors in the CFLint configuration
 *
 */
public class CFLintConfigurationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CFLintConfigurationException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public CFLintConfigurationException(final String arg0) {
        super(arg0);
    }

    public CFLintConfigurationException(final Throwable arg0) {
        super(arg0);
    }

}
