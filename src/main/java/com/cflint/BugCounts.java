
package com.cflint;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

public class BugCounts {
		final public static String[] levels = new String[] {"FATAL", "CRITICAL", "ERROR", "WARNING", "CAUTION", "INFO", "COSMETIC"};


		protected Map<String, Integer> severityCounts = new HashMap<String, Integer>();
		protected Map<String, Integer> bugCounts = new HashMap<String, Integer>();
		protected int noBugs = 0;

		public void add(String code, String severity) {
			if (bugCounts.get(code) == null) {
				bugCounts.put(code, 1);
			}
			else {
				bugCounts.put(code, bugCounts.get(code) + 1);
			}

			if (severityCounts.get(severity) == null) {
				severityCounts.put(severity, 1);
			}
			else {
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

		public int getCode(String code) {
			if (bugCounts.get(code) != null) {
				return bugCounts.get(code);
			}

			return 0;
		}

		public int getSeverity(String severity) {
			if (severityCounts.get(severity) != null) {
				return severityCounts.get(severity);
			}

			return 0;
		}
}