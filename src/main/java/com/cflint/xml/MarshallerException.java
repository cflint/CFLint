package com.cflint.xml;

public class MarshallerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MarshallerException() {
    }

    public MarshallerException(String message) {
        super(message);
    }

    public MarshallerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarshallerException(Throwable cause) {
        super(cause);
    }
}
