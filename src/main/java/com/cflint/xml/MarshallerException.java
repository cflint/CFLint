package com.cflint.xml;

public class MarshallerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MarshallerException() {
    }

    public MarshallerException(final String message) {
        super(message);
    }

    public MarshallerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MarshallerException(final Throwable cause) {
        super(cause);
    }
}
