package com.cflint;

public enum Levels {
    FATAL("FATAL"),
    CRITICAL("CRITICAL"),
    ERROR("ERROR"),
    WARNING("WARNING"),
    CAUTION("CAUTION"),
    INFO("INFO"),
    COSMETIC("COSMETIC"),
    UNKNOWN("UNKNOWN");

    private final String severity;

    private Levels(final String name) {
        severity = name;
    }

    @Override
    public String toString() {
        if (severity == null || this == UNKNOWN) {
            return "";
        }

        return this.severity;
    }

    public static Levels fromString(final String severity) {
        for (Levels level : Levels.values()) {
            if (level.toString().equals(severity)) {
                return level;
            }
        }

        return UNKNOWN;
    }
}
