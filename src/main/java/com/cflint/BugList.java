package com.cflint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cflint.tools.CFLintFilter;

public class BugList implements Iterable<BugInfo> {

    private Map<String, List<BugInfo>> bugs = new HashMap<>();
    private CFLintFilter filter;
    private CFLintFilter fileFilter;

    /**
     *
     */
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -8008927848087276202L;

    public BugList(final CFLintFilter filter) {
        super();
        this.filter = filter;
    }

    public CFLintFilter getFileFilter() {
        return fileFilter;
    }

    public CFLintFilter getFilter() {
        return filter;
    }

    public void setFilter(final CFLintFilter filter) {
        this.filter = filter;
        this.fileFilter = filter.createFilePreFilter();
    }

    public boolean add(final BugInfo bugInfo) {
        if (filter == null || filter.include(bugInfo)) {
            if (!bugs.containsKey(bugInfo.getMessageCode())) {
                bugs.put(bugInfo.getMessageCode(), new ArrayList<BugInfo>());
            }
            final List<BugInfo> curBugList = bugs.get(bugInfo.getMessageCode());
            return curBugList.add(bugInfo);
        } else {
            return false;
        }
    }

    /**
     * Returns a list of bugs as a map.
     *
     * @return Map of bugs
     */
    public Map<String, List<BugInfo>> getBugList() {
        return bugs;
    }

    public int size() {
        int size = 0;
        for (final List<?> list : bugs.values()) {
            size += list.size();
        }
        return size;
    }

    public List<BugInfo> getFlatBugList() {
        final List<BugInfo> retval = new ArrayList<>();
        for (final List<BugInfo> list : bugs.values()) {
            retval.addAll(list);
        }
        return retval;

    }

    @Override
    public Iterator<BugInfo> iterator() {
        return getFlatBugList().iterator();
    }

    public void clearBugList() {
        bugs.clear();
    }
}
