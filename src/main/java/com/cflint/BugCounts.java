
package com.cflint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BugCounts {
    protected Map<Levels, Integer> severityCounts = new HashMap<>();
    protected Map<String, Integer> codeCounts = new HashMap<>();
    protected int noBugs = 0;

    public void add(final String code, final Levels severity) {
        if (severity == Levels.UNKNOWN) {
            return;
        }

        if (codeCounts.get(code) == null) {
            codeCounts.put(code, 1);
        } else {
            codeCounts.put(code, codeCounts.get(code) + 1);
        }

        if (severityCounts.get(severity) == null) {
            severityCounts.put(severity, 1);
        } else {
            severityCounts.put(severity, severityCounts.get(severity) + 1);
        }
        noBugs++;
    }

    public int noBugs() {
        return noBugs;
    }

    public int noBugTypes() {
        return codeCounts.size();
    }

    public Set<String> bugTypes() {
        return codeCounts.keySet();
    }

    public int getCode(final String code) {
        if (codeCounts.get(code) != null) {
            return codeCounts.get(code);
        }

        return 0;
    }

    public int getSeverity(final Levels severity) {
        if (severityCounts.get(severity) != null) {
            return severityCounts.get(severity);
        }

        return 0;
    }
}
