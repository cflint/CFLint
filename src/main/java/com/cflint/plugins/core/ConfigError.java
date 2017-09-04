package com.cflint.plugins.core;

/**
 * Exception to throw if a configuration error occurs.
 */
public class ConfigError extends Exception {
    public ConfigError(final String message) {
        super(message);
    }
}
