package com.cflint.exception;

/**
 * Class to report exceptions in CFLint scanning.
 *
 */
public class CFLintScanException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CFLintScanException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public CFLintScanException(final String arg0) {
        super(arg0);
    }

    public CFLintScanException(final Throwable arg0) {
        super(arg0);
    }

}
