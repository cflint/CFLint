package com.cfbugs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cfbugs.tools.CFBugsFilter;

public class BugList {

	Map<String, List<BugInfo>> bugList = new HashMap<String, List<BugInfo>>();
	/**
	 * 
	 */
	private static final long serialVersionUID = -8008927848087276202L;

	public BugList(final CFBugsFilter filter) {
		super();
		this.filter = filter;
	}

	CFBugsFilter filter;

	public CFBugsFilter getFilter() {
		return filter;
	}

	public void setFilter(final CFBugsFilter filter) {
		this.filter = filter;
	}

	public boolean add(final BugInfo bugInfo) {
		if (filter == null || filter.include(bugInfo)) {
			if(!bugList.containsKey(bugInfo.getMessageCode())){
				bugList.put(bugInfo.getMessageCode(),new ArrayList<BugInfo>());
			}
			List<BugInfo> curBugList = bugList.get(bugInfo.getMessageCode());
			return curBugList.add(bugInfo);
		} else {
			return false;
		}
	}

	public Map<String, List<BugInfo>> getBugList() {
		return bugList;
	}
	
	public int size(){
		int size = 0;
		for(List<?> list: bugList.values()){
			size += list.size();
		}
		return size;
	}

}
