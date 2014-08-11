package com.cflint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cflint.tools.CFLintFilter;

public class BugList implements Iterable<BugInfo> {

	Map<String, List<BugInfo>> bugList = new HashMap<String, List<BugInfo>>();
	/**
	 * 
	 */
	private static final long serialVersionUID = -8008927848087276202L;

	public BugList(final CFLintFilter filter) {
		super();
		this.filter = filter;
	}

	CFLintFilter filter;

	public CFLintFilter getFilter() {
		return filter;
	}

	public void setFilter(final CFLintFilter filter) {
		this.filter = filter;
	}

	public boolean add(final BugInfo bugInfo) {
		if (filter == null || filter.include(bugInfo)) {
			if (!bugList.containsKey(bugInfo.getMessageCode())) {
				bugList.put(bugInfo.getMessageCode(), new ArrayList<BugInfo>());
			}
			final List<BugInfo> curBugList = bugList.get(bugInfo.getMessageCode());
			return curBugList.add(bugInfo);
		} else {
			return false;
		}
	}

	public Map<String, List<BugInfo>> getBugList() {
		return bugList;
	}

	public int size() {
		int size = 0;
		for (final List<?> list : bugList.values()) {
			size += list.size();
		}
		return size;
	}

	public List<BugInfo> getFlatBugList() {
		final List<BugInfo> retval = new ArrayList<BugInfo>();
		for (final List<BugInfo> list : bugList.values()) {
			retval.addAll(list);
		}
		return retval;

	}

	public Iterator<BugInfo> iterator() {
		return getFlatBugList().iterator();
	}
}
