
package com.cflint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BugCounts {
    protected Map<Levels, Integer> severityCounts = new HashMap<Levels, Integer>();
    protected Map<String, Integer> bugCounts = new HashMap<String, Integer>();
    protected int noBugs = 0;

    public void add(final String code, final Levels severity) {
        if (severity == Levels.UNKNOWN) {
            return;
        }

        if (bugCounts.get(code) == null) {
            bugCounts.put(code, 1);
        } else {
            bugCounts.put(code, bugCounts.get(code) + 1);
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
        return bugCounts.size();
    }

    public Set<String> bugTypes() {
        return bugCounts.keySet();
    }

    public int getCode(final String code) {
        if (bugCounts.get(code) != null) {
            return bugCounts.get(code);
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