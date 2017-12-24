package com.cflint.plugins.core;

/**
 * Exception to throw if a configuration error occurs.
 */
public class ConfigError extends Exception {
    private static final long serialVersionUID = 1L;

    public ConfigError(final String message) {
        super(message);
    }
}
