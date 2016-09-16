package com.cflint.xml;

public class MarshallerException extends Exception {

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
