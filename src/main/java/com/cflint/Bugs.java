package com.cflint;

/**
 * A few commonly use constants used when referring to bugs.
 */
public class Bugs {
    /**
     * Count.
     */
    public static final String COUNT = "count";

    /**
     * Code.
     */
    public static final String CODE = "code";
    
    /**
     * ID.
     */
    public static final String ID = "id";

    /**
     * Issue.
     */
    public static final String ISSUE = "issue";

    /**
     * Severity.
     */
    public static final String SEVERITY = "severity";

    private Bugs() {
          throw new IllegalStateException("Bugs utility class");
        }

}
